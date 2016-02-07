package controllers.db

import model.Customer

trait ICustomerDAO {
  def getCustomerById(id: Int) : Option[Customer]

  def getCustomerNames() : List[String]

  def getLowerCustomerNames(): List[String]

  def getCustomerByName(name: String) : Option[Customer]
}
