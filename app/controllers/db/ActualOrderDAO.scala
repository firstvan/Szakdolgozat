package controllers.db

//import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.Imports._


object ActualOrderDAO extends IActualOrderDAO{

  val format = new java.text.SimpleDateFormat("dd-MM-yyyy")

  override def addOrder(orderNumber: Int, productNumber: String, ordered: Int, actualPrice: Int): Unit = {
    ???
  }

  /**
    * This method will add a new record to orders table.
    */
  override def addNewOrder(salesManUserName: String, customerId : String, delivery: String): Int = {
    val orderNum = getOrderNum()

    val usr = UserDAO.getUserByUserName(salesManUserName).get._id

    val newOrder = MongoDBObject(
      "_id" -> orderNum,
      "sales_man_id" -> usr,
      "customer" -> customerId,
      "date_of_take" -> format.format(new java.util.Date()),
      "delivery_date" -> delivery
    )

    DBFactory.getCollection("orders").insert(newOrder)

    return orderNum
  }

  private def getOrderNum(): Int = {
    val query = MongoDBObject() // All documents
    val fields = MongoDBObject("_id" -> 1) // Only return `_id`
    val orderBy = MongoDBObject("_id" -> -1) // Order by _id descending

    val item = DBFactory.getCollection("orders").findOne(query, fields, orderBy)

    val id = item.get.as[Int]("_id")
    println(id)

    return id + 1
  }

}
