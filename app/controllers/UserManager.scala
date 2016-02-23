package controllers

import controllers.db.UserDAO
import play.api.mvc.Controller
import com.github.t3hnar.bcrypt._

/**
  * Created by Firstvan on 2016. 02. 23..
  */
class UserManager extends Controller with Secured {

  def index = withAuth { username => implicit request =>
    val list = UserDAO.getUserList("Manager")

    Ok(views.html.UserManager(username, list))
  }

  def modifyUser(id: Int) = withAuth { username => implicit request =>
    if(id == 0){
      Ok(views.html.UserModify(username, null))
    } else {
      val user = UserDAO.getUserByUserID(id)

      Ok(views.html.UserModify(username, user.get))
    }
  }

  def saveUser() = withAuth {username => implicit request =>
    val map : Map[String,Seq[String]] = request.body.asFormUrlEncoded.getOrElse(Map())

    val id: Seq[String] = map.getOrElse("id", List[String]())
    val usrname: Seq[String] = map.getOrElse("username", List[String]())
    val fullname: Seq[String] = map.getOrElse("fullname", List[String]())
    val pass: Seq[String] = map.getOrElse("pass", List[String]())
    var retid = id.head

    if(id.head.toInt == 0) {
      val succ = UserDAO.insertUser(usrname.head, fullname.head, pass.head.bcrypt)
      if(!succ){
        retid = "-1"
      }
    } else {
      if (pass.isEmpty) {
        UserDAO.saveUser(id.head.toInt, usrname.head, fullname.head, "")
      } else {
        UserDAO.saveUser(id.head.toInt, usrname.head, fullname.head, pass.head.bcrypt)
      }
    }

    Ok(retid)
  }
}
