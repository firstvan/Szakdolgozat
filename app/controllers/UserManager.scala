package controllers

import controllers.db.UserDAO
import play.api.mvc.Controller
import com.github.t3hnar.bcrypt._

/**
  * Created by Firstvan on 2016. 02. 23..
  */
class UserManager extends Controller with Secured {

  def index = withUser("admin") { username => implicit request =>
    val list = UserDAO.getAllUser()

    Ok(views.html.UserManager(username.username, list))
  }

  def modifyUser(id: Int) = withUser("admin") { username => implicit request =>
    if(id == 0){
      Ok(views.html.UserModify(username.username, null))
    } else {
      val user = UserDAO.getUserByUserID(id)

      Ok(views.html.UserModify(username.username, user.get))
    }
  }

  def modifyAccount = withAuth { username => implicit request =>
      val user = UserDAO.getUserByUserName(username)

      if(user.get.accountType.equals("Manager")){
        Ok(views.html.UserModify(username, user.get, user.get._id))
      } else {
        Ok(views.html.UserModify(username, user.get))
      }
  }

  def saveUser() = withAuth {username => implicit request =>
    val map : Map[String,Seq[String]] = request.body.asFormUrlEncoded.getOrElse(Map())

    val id: Seq[String] = map.getOrElse("id", List[String]())
    val usrname: Seq[String] = map.getOrElse("username", List[String]())
    val fullname: Seq[String] = map.getOrElse("fullname", List[String]())
    val pass: Seq[String] = map.getOrElse("pass", List[String]())
    val t: Seq[String] = map.getOrElse("type", List[String]())

    val usr = UserDAO.getUserByUserID(id.head.toInt)
    var retid = id.head


    if(id.head.toInt == 0) {
      val succ = UserDAO.insertUser(usrname.head, fullname.head, pass.head.bcrypt, t.head)
      if(!succ){
        retid = "-1"
      }
    } else {
      if(usrname.head.equals(username)){
        retid = "1"
      } else {
        retid = "2"
      }
      if (pass.head.isEmpty) {
        UserDAO.saveUser(id.head.toInt, usrname.head, fullname.head, "")
      } else {
        UserDAO.saveUser(id.head.toInt, usrname.head, fullname.head, pass.head.bcrypt)
      }
    }

    Ok(retid)
  }

  def removeUser(id: Int) = withUser("admin") { username => implicit request =>
    val ret = UserDAO.deleteUser(id)
    Ok("")
  }
}
