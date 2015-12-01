package controllers

import model.User
import controllers.db.{UserDAO}
import play.api.mvc._

trait Secured {
  self: Controller =>
  def username(request: RequestHeader) = request.session.get(Security.username)

  def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.index)

  def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) {
      user => Action(request => f(user)(request))
    }
  }

  def withUser(f: User => Request[AnyContent] => Result) = withAuth { username => implicit request =>
    UserDAO.getUserByUserName(username).map { user =>
      f(user)(request)
    }.getOrElse(onUnauthorized(request))
  }
}
