package controllers.db

import model.Customer

trait ICustomerDAO {
  def getCustomerById(id: Int) : Option[Customer]

  def getCustomerNames() : List[String]

  def getLowerCustomerNames(): List[String]

  def getCustomerByName(name: String) : Option[Customer]

  /**
    * List of customers
    *
    * @return
    */
  def getCustomerList(name: String) : List[Customer]

  /**
    * Insert a customer to database.
    * @param name
    * @param add
    * @param payment
    * @return
    */
  def insertCustomer(name: String, add: String, payment: String): Boolean

  /**
    * Modify user by params.
    * @param id
    * @param name
    * @param addr
    * @param payment
    */
  def saveCustomer(id: Int, name: String, addr: String, payment: String): Unit

  /**
    * Delete customer.
    * @param id
    */
  def deleteCustomer(id: Int): Unit

  /**
    * Get customers list.
    * @return list of cusomters.
    */
  def getCustomers() : List[Customer]

  /**
    * Return list of customer by code.
    * @param code
    * @return
    */
  def getCustomerListByCode(code: Int) : List[Customer]

  /**
    * Insert a new customer to request table.
    * @param name name of customer
    * @param add address of customer
    * @param payment payment of customer
    * @return success
    */
  def insertRequest(name: String, add: String, payment: String): Boolean
}
