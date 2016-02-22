package controllers.db

import model.User

trait IUserDAO {

  def getUserByUserName(name: String) : Option[User]

  def insertUser(user: User): Unit

  def getUserByUserID(id: Int) : Option[User]
}
