package controllers

import java.util

import controllers.db.UserManager

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._


class Login extends Controller {

  val loginForm = Form(
    tuple(
      "usrname" -> text,
      "pwd" -> text
    )
  )

  def login = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Redirect("Not succes"),
      user => Redirect("mainpage")
    )
  }

  def register = Action {
    val usr = UserManager.getUserByUserName("admin")
    if(usr == None){
      Ok("None")
    }
    val str = usr.get.username + " " + usr.get.fullname
    Ok(str)
  }

}