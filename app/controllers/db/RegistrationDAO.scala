package controllers.db

import com.mongodb.casbah.commons.{MongoDBList, MongoDBObject}
import com.mongodb.casbah.Imports._
import model.{OrderState, Orders, Item, Registration}
import org.joda.time.{DateTimeComparator, DateTime}
import org.joda.time.format.DateTimeFormat

import scala.collection.mutable.ListBuffer

object RegistrationDAO extends IRegistrationDAO{
  val collection = DBFactory.getCollection("orders")
  val prod_collection = DBFactory.getCollection("ordered_products")

  override def getRegistrationByCustomerId(id: Int): Option[List[Orders]] = {

    val itemListObj = collection.find(MongoDBObject("cust_no" -> id))

    if(itemListObj.isEmpty)
      return None

    val returnList = ListBuffer[Orders]()

    for (x <- itemListObj) {
      returnList.append(getRegistration(x))
    }

    Some(returnList.toList)
  }

  override def getRegistrationByUser(user_id: Int, opened: Int): Option[List[Orders]] = {
    var itemListObj :MongoCursor = null
    if(user_id == 0) {
      itemListObj = collection.find(MongoDBObject("opened" -> opened))
    } else {
      itemListObj = collection.find(MongoDBObject("sales_man_id" -> user_id, "opened" -> opened))
    }

    if(itemListObj.isEmpty)
       return None

    val returnList = ListBuffer[Orders]()

    for (x <- itemListObj){
      returnList.append(getRegistration(x))
    }

    Some(returnList.toList)
  }


  private def getRegistration(item: RegistrationDAO.this.collection.T) : Orders = {

    val format = "dd-MM-yyyy"
    val indate = DateTime.parse(item.get("date_of_take").toString, DateTimeFormat.forPattern(format))
    val outdate = DateTime.parse(item.get("delivery_date").toString, DateTimeFormat.forPattern(format))

    new Orders(item.getAs[Int]("_id").get, item.getAs[Int]("sales_man_id").get,
     item.getAs[Int]("customer").get, indate, outdate, item.getAs[Int]("opened").get,
     item.getAs[Int]("total").get, item.getAs[Int]("d_total").get)
  }

  override def getOrderById(id: Int): Orders = {

    val order = collection.findOne(MongoDBObject("_id" -> id)).get

    getRegistration(order)
  }

  /**
    * Return a list of orders.
    *
    * @param name  name of customer
    * @param start start of list date
    * @param end   end of list date
    * @return
    */
  override def getOrders(name: String, start: String, end: String, id: Int): List[Orders] = {
    val format = "yyyy-MM-dd"

    val startDate = DateTime.parse(start, DateTimeFormat.forPattern(format))
    val endDate = DateTime.parse(end, DateTimeFormat.forPattern(format))

    val retList = new ListBuffer[Orders]()

    if(!name.isEmpty) {
      val users = CustomerDAO.getCustomersByName(name)
      for(user <- users){
        var query = MongoDBObject("opened"-> OrderState.Closed, "customer"-> user._id)
        if(id != 0) {
          query = MongoDBObject("opened"-> OrderState.Closed, "customer"-> user._id, "sales_man_id" -> id)
        }
        val res = collection.find(query)
        for(x <- res){
          val ord = getRegistration(x)

          if(ord.date_of_take.getMillis <= endDate.getMillis && ord.date_of_take.getMillis >= startDate.getMillis) {
            retList += ord
          }
        }
      }
    } else {
      var query = MongoDBObject("opened"-> OrderState.Closed)

      if(id != 0) {
        query = MongoDBObject("opened"-> OrderState.Closed, "sales_man_id" -> id)
      }
      val res = collection.find(query)
      for(x <- res){
        val ord = getRegistration(x)

        if(ord.date_of_take.getMillis <= endDate.getMillis && ord.date_of_take.getMillis >= startDate.getMillis) {
          retList += ord
        }
      }
    }

    retList.toList
  }
}
