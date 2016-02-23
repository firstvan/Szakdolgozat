package controllers.db

//import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.Imports._
import model.User

import scala.collection.mutable.ListBuffer


object UserDAO extends IUserDAO{
  val collection = DBFactory.getCollection("users")

  override def getUserByUserName(name: String) : Option[User] = {
    val usrObj = collection.findOne(MongoDBObject("username" -> name))

    if(usrObj.isEmpty)
      return None

    val usr = usrObj.get

    val returnUser = new User(usr.as[Int]("_id"), name, usr.get("passwordHash").toString,
      usr.get("fullname").toString, usr.get("accountType").toString)

    Some(returnUser)
  }

  override def insertUser(user: User): Unit = {
    val dbObject = DBFactory.buildMongoDbObject(user)
    collection.save(dbObject)
  }

  override def insertUser(usrname: String, fullname: String, pass: String): Boolean = {

    val list = getUserList("Manager")

    for(u <- list){
      if (u.username.equals(usrname)){
        return false
      }
    }

    val id = getIdNum()

    val newUser = MongoDBObject (
      "_id" -> id,
      "username" -> usrname,
      "fullname" -> fullname,
      "passwordHash" -> pass,
      "accountType" -> "Manager"
    )

    collection.insert(newUser)
    true
  }

  override def getUserByUserID(id: Int): Option[User] = {
    val usrObj = collection.findOne(MongoDBObject("_id" -> id))

    if(usrObj.isEmpty)
      return None

    val usr = usrObj.get

    val returnUser = getUser(usr)

    Some(returnUser)
  }

  /**
    * Get list of users by type
    *
    * @return
    */
  override def getUserList(t: String): List[User] = {
    val query = MongoDBObject("accountType" -> t)

    val res = collection.find(query)

    val returnList = ListBuffer[User]()

    for(p <- res){
      returnList += getUser(p)
    }

    returnList.toList.sortWith((a,b) => if (a.fullname < b.fullname) true; else false)
  }

  private def getUser(usr: UserDAO.this.collection.T): User = {
    new User(usr.as[Int]("_id"), usr.getAs[String]("username").get, usr.get("passwordHash").toString,
      usr.get("fullname").toString, usr.get("accountType").toString)
  }

  /**
    * Change user settings.
    *
    * @param id
    * @param username
    * @param fullname
    * @param pass
    */
  override def saveUser(id: Int, username: String, fullname: String, pass: String): Unit = {
    val query = MongoDBObject("_id" -> id)

    if(pass.isEmpty){
      collection.findAndModify(query, $set("username"-> username, "fullname"-> fullname))
    } else {
      collection.findAndModify(query, $set("username"-> username, "fullname"-> fullname, "passwordHash"-> pass))
    }

  }

  /**
    * Get the max id of user to autoincrement.
    *
    * @return Int of max id
    */
  private def getIdNum(): Int = {
    val query = MongoDBObject() // All documents
    val fields = MongoDBObject("_id" -> 1) // Only return `_id`
    val orderBy = MongoDBObject("_id" -> -1) // Order by _id descending

    val item = collection.findOne(query, fields, orderBy)

    if(item.isEmpty){
      return 0
    }

    val id = item.get.as[Int]("_id")

    id + 1
  }
}




