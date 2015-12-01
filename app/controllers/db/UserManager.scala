package controllers.db

import com.mongodb.casbah.commons.MongoDBObject

case class User (_id: String, username: String, passwordHash: String, fullname: String, accountType: String)

object UserManager {
  val collection = DBFactory.getCollection("users")

  def getUserByUserName(name: String) : Option[User] = {
    val usrObj = collection.findOne(MongoDBObject("username" -> name))


    if(usrObj.isEmpty)
      return None

    val usr = usrObj.get

    if(usr.get("username") == null)
      return None

    val returnUser = new User(usr.get("_id").toString(), name, usr.get("passwordHash").toString(),
      usr.get("fullname").toString(), usr.get("accountType").toString())

    return Some(returnUser)
  }

  def insertUser(user: User): Unit = {
    val dbObject = DBFactory.buildMongoDbObject(user)
    collection.save(dbObject)
  }

}




