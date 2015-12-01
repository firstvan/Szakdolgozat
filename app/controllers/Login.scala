package controllers

import java.util

import controllers.db.{User, UserManager}

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import com.github.t3hnar.bcrypt._

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
    val usr = new User("3", "admin1", "admin1".bcrypt, "Admin 1", "Administrator")

    UserManager.insertUser(usr)
    Ok("Succes")
  }

  def checkPasswd = Action {
    val usr = UserManager.getUserByUserName("admin1")

    if ("admin1".isBcrypted(usr.get.passwordHash)){
      Ok("succes")
    } else {
      Ok("False")
    }
  }

}