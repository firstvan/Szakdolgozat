package controllers.db

import com.mongodb.casbah.Imports._
import model.OrderedItem


object ActualOrderDAO extends IActualOrderDAO{

  private val collectionOrder = DBFactory.getCollection("orders")
  private val collectionItems = DBFactory.getCollection("ordered_products")

  val format = new java.text.SimpleDateFormat("dd-MM-yyyy")

  /**
    * Add product to orders.
    *
    * @param orderNumber number of order
    * @param productNumber number of product
    * @param ordered piece of ordered product
    * @param actualPrice actual price for history
    */
  override def addOrder(orderNumber: Int, productNumber: String, ordered: Int, actualPrice: Int): Unit = {
    val item = collectionItems.findOne(MongoDBObject("order_id" -> orderNumber, "product_number" -> productNumber))

    if(item.isEmpty) {

      val newProduct = MongoDBObject(
        "_id" -> getOrderNum("ordered_products"),
        "order_id" -> orderNumber,
        "product_number" -> productNumber,
        "ordered_piece" -> ordered,
        "ordered_price" -> actualPrice,
        "deliveried" -> 0
      )

      collectionItems.insert(newProduct)
    } else {

      val neworder = ordered

      collectionItems.update(MongoDBObject("_id" -> item.get.get("_id")), $set ("ordered_piece" -> neworder))
    }
  }

  /**
    * This method will add a new record to orders table.
    */
  override def addNewOrder(salesManUserName: String, customerName : String, delivery: String): Int = {
    val orderNum = getOrderNum("orders")

    val usr = UserDAO.getUserByUserName(salesManUserName).get._id
    val cust = CustomerDAO.getCustomerByName(customerName).get._id

    val newOrder = MongoDBObject(
      "_id" -> orderNum,
      "sales_man_id" -> usr,
      "customer" -> cust,
      "date_of_take" -> format.format(new java.util.Date()),
      "delivery_date" -> delivery
    )

    collectionOrder.insert(newOrder)

    return orderNum
  }


  /**
    * Get the max id of ordered_products to autoincrement.
    *
    * @return Int of max id
    */
  private def getOrderNum(collectionName: String): Int = {
    val query = MongoDBObject() // All documents
    val fields = MongoDBObject("_id" -> 1) // Only return `_id`
    val orderBy = MongoDBObject("_id" -> -1) // Order by _id descending

    val item = DBFactory.getCollection(collectionName).findOne(query, fields, orderBy)

    if(item.isEmpty){
      return 0
    }

    val id = item.get.as[Int]("_id")
    //println(id)

    return id + 1
  }

  override def getOrderedProducts(actualOrder: Int): Map[String, Int] = {
    var retDict: Map[String, Int] = Map()
    val productsObj = collectionItems.find(MongoDBObject("order_id" -> actualOrder))

    for(x <- productsObj){
      val prod = getProduct(x)
      retDict += (prod.product_number -> prod.ordered_piece)
    }

    return retDict
  }

  private def getProduct(item: ActualOrderDAO.this.collectionItems.T) : OrderedItem = {
    return new OrderedItem(item.getAs[Int]("_id").get,
      item.getAs[Int]("order_id").get,
      item.getAs[String]("product_number").get,
      item.getAs[Int]("ordered_piece").get,
      item.getAs[Int]("ordered_price").get,
      item.getAs[Int]("deliveried").get
    )
  }
}
