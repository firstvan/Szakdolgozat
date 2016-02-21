package controllers

import controllers.db.UserDAO
import model.User

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
      user => {

        if(checkPasswd(user._1, user._2)){
          Main.user = UserDAO.getUserByUserName(user._1).get
          Redirect(routes.Main.index).withSession(Security.username -> user._1)
        }else {
          Ok(views.html.index("Vendég", "Hibás felhasználónév vagy jelszó!"))
        }
      }
    )
  }

  def register = Action {
    val usr = new User(1, "admin", "admin".bcrypt, "Admin", "admin")

    UserDAO.insertUser(usr)
    Ok("Succes")
  }

  def checkPasswd(usrName :String, passwd :String) : Boolean = {
    val usr = UserDAO.getUserByUserName(usrName)

    if(usr.isEmpty) {
      return false
    }
    else {
      if(passwd.isBcrypted(usr.get.passwordHash)) {
        return true
      }
    }

    return false
  }

  def logout = Action {
    Redirect(routes.Application.index).withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }

}