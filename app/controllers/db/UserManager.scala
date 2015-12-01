package controllers.db

import com.mongodb.casbah.commons.MongoDBObject

case class User (id: String, username: String, passwordHash: String, fullname: String, accountType: String)

object UserManager {
  val collection = DBFactory.getCollection("users")

  def getUserByUserName(name: String) : Option[User] = {
    val usrObj = collection.findOne(MongoDBObject("username" -> name))


    if(usrObj == None)
      return None

    val usr = usrObj.get

    val returnUser = new User(usr.get("_id").toString(),
      usr.get("username").toString(), usr.get("password").toString(),
      usr.get("fullname").toString(), usr.get("accountType").toString())

    return Some(returnUser)
  }

}




