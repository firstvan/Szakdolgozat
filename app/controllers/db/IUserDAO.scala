package controllers.db

import model.User

trait IUserDAO {

  def getUserByUserName(name: String): Option[User]

  def insertUser(user: User): Unit

  def getUserByUserID(id: Int): Option[User]

  /**
    * Get list of users by type
    *
    * @return
    */
  def getUserList(t: String): List[User]

  /**
    * Change user settings.
    *
    * @param id
    * @param username
    * @param fullname
    * @param pass
    */
  def saveUser(id: Int, username: String, fullname: String, pass: String): Unit

  /**
    * Insert user by params
    * @param usrname
    * @param fullname
    * @param pass
    */
  def insertUser(usrname: String, fullname: String, pass: String, t: String): Boolean

  def getAllUser() : List[User]
}
