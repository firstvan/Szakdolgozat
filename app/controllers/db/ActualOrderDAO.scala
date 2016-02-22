package controllers.db

import com.mongodb.casbah.Imports._
import model.{OrderState, OrderedItem}


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
    val order = collectionOrder.findOne(MongoDBObject("_id" -> orderNumber))
    var total: Int = 0

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
      total = order.get.getAs[Int]("total").get + (ordered * actualPrice)

    } else {

      val neworder = ordered

      val old_piece = item.get.getAs[Int]("ordered_piece").get
      val old_price = item.get.getAs[Int]("ordered_price").get

      collectionItems.update(MongoDBObject("_id" -> item.get.get("_id")), $set ("ordered_piece" -> neworder))
      collectionItems.update(MongoDBObject("_id" -> item.get.get("_id")), $set ("ordered_price" -> actualPrice))


      total = order.get.getAs[Int]("total").get - (old_piece * old_price) + (ordered * actualPrice)

    }

    collectionOrder.update(MongoDBObject("_id" -> orderNumber), $set ("total" -> total))

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
      "delivery_date" -> delivery,
      "opened" -> OrderState.Open,
      "total" -> 0,
      "d_total" -> 0
    )

    collectionOrder.insert(newOrder)

    orderNum
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

    id + 1
  }

  override def getOrderedProducts(actualOrder: Int): Map[String, Int] = {
    var retDict: Map[String, Int] = Map()
    val productsObj = collectionItems.find(MongoDBObject("order_id" -> actualOrder))

    for(x <- productsObj){
      val prod = getProduct(x)
      retDict += (prod.product_number -> prod.ordered_piece)
    }

    retDict
  }

  private def getProduct(item: ActualOrderDAO.this.collectionItems.T) : OrderedItem = {
    new OrderedItem(item.getAs[Int]("_id").get,
      item.getAs[Int]("order_id").get,
      item.getAs[String]("product_number").get,
      item.getAs[Int]("ordered_piece").get,
      item.getAs[Int]("ordered_price").get,
      item.getAs[Int]("deliveried").get
    )
  }

  /**
    * Delete a specify item from order list.
    *
    * @param prodNo product number
    * @return delete was success.
    */
  override def deleteItem(orderid: Int, prodNo: String): Int = {

    val query = MongoDBObject("order_id" -> orderid, "product_number" -> prodNo)

    val item = collectionItems.findOne(query)

    val order = collectionOrder.findOne(MongoDBObject("_id" -> orderid))

    val old_piece = item.get.getAs[Int]("ordered_piece").get
    val old_price = item.get.getAs[Int]("ordered_price").get

    val total = order.get.getAs[Int]("total").get - (old_piece * old_price)

    collectionOrder.update(MongoDBObject("_id" -> orderid), $set ("total" -> total))

    val remove = collectionItems.findAndRemove(query)

    if(remove.isEmpty)
      1
    else
      0
  }

  /**
    * Delete order and orderd products by id
    *
    * @param orderid order id
    * @return 1 if success
    */
  override def deleteOrder(orderid: Int): Int = {
    var query = MongoDBObject("_id" -> orderid)

    val remove = collectionOrder.findAndRemove(query)

    if(remove.isDefined){
      query = MongoDBObject("order_id" -> orderid)
      collectionItems.remove(query)
      0
    } else{
      1
    }
  }

  /**
    * Return the total of order by order id.
    *
    * @param orderid
    * @return total of order
    */
  override def getTotal(orderid: Int): Int = {
    val query = MongoDBObject("_id" -> orderid)

    val get = collectionOrder.findOne(query).get

    get.getAs[Int]("total").get
  }

  /**
    * Update delevery time.
    *
    * @param orderid id of order
    * @param time    time to update
    * @return 0 if success
    */
  override def updateTime(orderid: Int, time: String): Int = {
    val query = MongoDBObject("_id" -> orderid)

    val dates = time.split("-")

    val formattedTime = dates(2) + "-" + dates(1) + "-" + dates(0)

    val res = collectionOrder.findAndModify(query, $set("delivery_date" -> formattedTime))

    if(res.isDefined){
      0
    } else {
      1
    }
  }

  /**
    * Set opened flag to 1;
    *
    * @param orderid
    * @return success
    */
  override def closeOrder(orderid: Int): Int = {
    val query = MongoDBObject("_id" -> orderid)

    val res = collectionOrder.findAndModify(query, $set("opened" -> 1))

    if(res.isDefined){
      0
    } else {
      1
    }
  }

  /**
    * Set delevery_piece to db.
    *
    * @param orderid
    * @param prodnumber productnumber
    * @param db         orderd piece
    * @return success
    */
  override def closeItem(orderid: Int, prodnumber: String, db: Int): (Int,Int) = {
    val query = MongoDBObject("order_id" -> orderid, "product_number" -> prodnumber)

    val item = collectionItems.findOne(query)
    val oldD = item.get.getAs[Int]("deliveried").get
    var del = 0
    val sub = db - oldD
    del = ProductDAO.updateStock(prodnumber, db, sub)

    val res = collectionItems.findAndModify(query, $set("deliveried" -> del))

    val query2 = MongoDBObject("_id" -> orderid)
    val order = collectionOrder.findOne(query2)

    val oldnum = item.get.getAs[Int]("deliveried").get
    var d_total = 0
    if(oldnum == 0){
      d_total = order.get.getAs[Int]("d_total").get + res.get.getAs[Int]("ordered_price").get * db
    } else {
      d_total = order.get.getAs[Int]("d_total").get + res.get.getAs[Int]("ordered_price").get * (db - oldnum)
    }

    val res2 = collectionOrder.findAndModify(query2, $set("d_total" -> d_total))

    (del, sub)
  }

  /**
    * Return ordered and deliveried piece
    *
    * @param id order id
    * @return
    */
  override def getOrderedAndDeliveriedProducts(id: Int): Map[String, (Int, Int)] = {
    var retDict: Map[String, (Int, Int)] = Map()
    val productsObj = collectionItems.find(MongoDBObject("order_id" -> id))


    for(x <- productsObj){
      val prod = getProduct(x)
      retDict += (prod.product_number -> (prod.ordered_piece, prod.delivered))
    }
    retDict
  }

  /**
    * Return the delivery total of delivered order by order id.
    *
    * @param orderid
    * @return total of order
    */
  override def getDeliveryTotal(orderid: Int): Int = {
    val query = MongoDBObject("_id" -> orderid)

    val get = collectionOrder.findOne(query).get

    get.getAs[Int]("d_total").get
  }
}
