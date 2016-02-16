package controllers.db

import model.OrderedItem

trait IActualOrderDAO {
  def addOrder(orderNumber: Int, productNumber: String ,ordered: Int, actualPrice: Int)

  /**
    * This method will add a new record to orders table.
    */
  def addNewOrder(salesManUserName: String, customerId : String, delivery: String) : Int

  /**
    * List the ordered products
    *
    * @param actualOrder
    * @return
    */
  def getOrderedProducts(actualOrder: Int) : Map[String, Int]

  /**
    * Delete a specify item from order list.
    * @param prodNo product number
    * @return delete was success.
    */
  def deleteItem(orderid: Int, prodNo: String) : Int

  /**
    * Delete order and orderd products by id
    * @param orderid order id
    * @return 1 if success
    */
  def deleteOrder(orderid: Int):Int
}
