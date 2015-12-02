package controllers.db

import com.mongodb.casbah.commons.{MongoDBList, MongoDBObject}
import com.mongodb.casbah.Imports._
import model.{Item, Registration}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.collection.mutable.ListBuffer

object RegistrationDAO extends IRegistrationDAO{
  val collection = DBFactory.getCollection("orders")

  override def getRegistrationByCustomerId(id: Int): Option[List[Registration]] = {

    val itemListObj = collection.find(MongoDBObject("cust_no" -> id))

    if(itemListObj.isEmpty)
      return None

    val returnList = ListBuffer[Registration]()

    for (x <- itemListObj) {
      returnList.append(getRegistration(x))
    }

    return Some(returnList.toList)
  }

  override def getRegistrationByUser(userId: Int, opened: Int): Option[List[Registration]] = {
    val itemListObj = collection.find(MongoDBObject("user_no" -> userId, "opened" -> opened))

    if(itemListObj.isEmpty)
      return None

    val returnList = ListBuffer[Registration]()

    for (x <- itemListObj){
      returnList.append(getRegistration(x))
    }

    return Some(returnList.toList)
  }


  private def getRegistration(item: RegistrationDAO.this.collection.T) : Registration = {

    val format = "yyyy-MM-dd HH:mm:ss"
    val indate = DateTime.parse(item.get("incomeDate").toString(), DateTimeFormat.forPattern(format))
    val outdate = DateTime.parse(item.get("shippingDate").toString(), DateTimeFormat.forPattern(format))

    val listOfOrdered = item.getAs[MongoDBList]("products").get

    val ordered = ListBuffer[Item]()

    for (item_ <- listOfOrdered){
      val myString = item_.toString()
      val i = myString.substring(1, myString.length - 1)
        .split(",")
        .map(_.split(":"))
        .map { case Array(k, v) => (k.substring(1, k.length-1), v.substring(1, v.length-1))}
        .toMap

      val a = i.get("\"ordered\"").get.replaceAll("\"", "").toInt
      val b = i.get("\"price\"").get.replaceAll("\"", "").toInt

      val it = new Item(i.get("\"productnumber\"").get.replaceAll("\"", ""), a, b)
      ordered.append(it)
    }

    return new Registration(item.getAs[Int]("_id").get, item.getAs[Int]("user_no").get,
      item.getAs[Int]("cust_no").get, ordered.toList, item.getAs[Int]("total").get,
      item.getAs[Int]("opened").get, indate, outdate)
  }

}
