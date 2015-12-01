package controllers

import play.api._
import play.api.mvc._

class Main extends Controller with Secured{

  def index = withAuth { username => implicit request =>
    Ok(views.html.mainpage(username))
  }
}
