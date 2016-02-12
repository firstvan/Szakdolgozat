package controllers.db

import model.Orders

trait IRegistrationDAO {
  def getRegistrationByCustomerId(id: Int): Option[List[Orders]]

  def getRegistrationByUser(user_id: Int, opened :Int): Option[List[Orders]]

  def getOrderById(id: Int) : Orders
}
