package controllers

import play.api._
import play.api.mvc._

import db.RegistrationDAO

class Main extends Controller with Secured{

  def index = withAuth { username => implicit request =>
    Ok(views.html.mainpage(username))
  }

  def reg = Action {
    RegistrationDAO.getRegistrationByCustomerId(3)
    Ok(views.html.mainpage(""))
  }
}
