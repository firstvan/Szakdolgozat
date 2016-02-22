package controllers.db

//import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.Imports._
import model.User


object UserDAO extends IUserDAO{
  val collection = DBFactory.getCollection("users")

  override def getUserByUserName(name: String) : Option[User] = {
    val usrObj = collection.findOne(MongoDBObject("username" -> name))

    if(usrObj.isEmpty)
      return None

    val usr = usrObj.get

    val returnUser = new User(usr.as[Int]("_id"), name, usr.get("passwordHash").toString(),
      usr.get("fullname").toString(), usr.get("accountType").toString())

    Some(returnUser)
  }

  override def insertUser(user: User): Unit = {
    val dbObject = DBFactory.buildMongoDbObject(user)
    collection.save(dbObject)
  }

  override def getUserByUserID(id: Int): Option[User] = {
    val usrObj = collection.findOne(MongoDBObject("_id" -> id))

    if(usrObj.isEmpty)
      return None

    val usr = usrObj.get

    val returnUser = new User(usr.as[Int]("_id"), usr.getAs[String]("fullname").get, usr.get("passwordHash").toString,
      usr.get("fullname").toString, usr.get("accountType").toString)

    Some(returnUser)
  }
}




