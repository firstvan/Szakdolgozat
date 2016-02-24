package controllers.db

import model.Orders

trait IRegistrationDAO {
  def getRegistrationByCustomerId(id: Int): Option[List[Orders]]

  def getRegistrationByUser(user_id: Int, opened :Int): Option[List[Orders]]

  def getOrderById(id: Int) : Orders

  /**
    * Return a list of orders.
    * @param name name of customer
    * @param start start of list date
    * @param end end of list date
    * @return
    */
  def getOrders(name: String, start: String, end: String):List[Orders]
}
