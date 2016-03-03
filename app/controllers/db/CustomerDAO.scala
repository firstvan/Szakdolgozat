package controllers.db

import com.mongodb.casbah.commons.{TypeImports, MongoDBObject}
import model.Customer
import com.mongodb.casbah.Imports._

import scala.collection.mutable.ListBuffer

object CustomerDAO extends ICustomerDAO{
  val collection = DBFactory.getCollection("customers")
  val collectionRequest = DBFactory.getCollection("request")

  override def getCustomerById(id: Int): Option[Customer] = {
    val cust = collection.findOne(MongoDBObject("_id" -> id))

    if(cust.isEmpty)
      return None

    Some(getCustomer(cust.get))
  }


  override def getCustomerNames(): List[String] = {
    val cust = collection.find()

    val return_list = ListBuffer[String]()

    for(c <- cust){
      return_list.append(c.getAs[String]("name").get)
    }

    return_list.toList
  }

  override def getLowerCustomerNames(): List[String] = {
    val cust = collection.find()

    val return_list = ListBuffer[String]()

    for(c <- cust){
      return_list.append(c.getAs[String]("lowerName").get)
    }

    return_list.toList
  }

  private def getCustomer(item: CustomerDAO.this.collection.T) : Customer = {
    new Customer(item.getAs[Int]("_id").get, item.getAs[String]("name").get,
      item.getAs[String]("billing_name").get, item.getAs[String]("billing_address").get,
      item.getAs[String]("p_type").get, item.getAs[String]("lowerName").get, item.getAs[Int]("code").get)
  }

  override def getCustomerByName(name: String): Option[Customer] = {
    val cust = collection.findOne(MongoDBObject("lowerName" -> name.toLowerCase))

    if(cust.isEmpty){
      return None
    }

    Some(getCustomer(cust.get))
  }

  def getCustomersByName(name: String): List[Customer] = {
    val nameWithRegex = "^" + name.toLowerCase() + ".*"
    val cust = collection.find(MongoDBObject("lowerName" -> nameWithRegex.r))

    val custList = new ListBuffer[Customer]

    for(x <- cust){
      custList += getCustomer(x)
    }

    custList.toList
  }

  /**
    * List of customers
    *
    * @return
    */
  override def getCustomerList(name :String): List[Customer] = {
    val nameWithRegex = "^" + name.toLowerCase() + ".*"
    val query = MongoDBObject("lowerName" -> nameWithRegex.r)

    val li = collection.find(query)
    val result = new ListBuffer[Customer]()

    for(x <- li){
      result += getCustomer(x)
    }

    result.toList.sortWith((a, b) => if (a.name < b.name) true; else false)
  }

  /**
    * Insert a customer to database.
    *
    * @param name
    * @param add
    * @param payment
    * @return
    */
  override def insertCustomer(name: String, add: String, payment: String): Boolean = {
    val list = getCodes()
    var rndNumber = scala.util.Random.nextInt(10000)

    while(list.contains(rndNumber)){
      rndNumber = scala.util.Random.nextInt(10000)
    }

    val id = getIdNum()

    val newCustomer = MongoDBObject (
      "_id" -> id,
      "code" -> rndNumber,
      "name" -> name,
      "lowerName" -> name.toLowerCase,
      "billing_name" -> name,
      "p_type" -> payment,
      "billing_address" -> add
    )

    collection.insert(newCustomer)
    true
  }

  /**
    * Modify user by params.
    *
    * @param id
    * @param name
    * @param addr
    * @param payment
    */
  override def saveCustomer(id: Int, name: String, addr: String, payment: String): Unit = {
    val query = MongoDBObject("_id" -> id)

    collection.findAndModify(query, $set("name"->name, "lowerName"->name.toLowerCase, "billing_name"->name,
      "billing_address"-> addr, "p_type" ->payment))
  }

  /**
    * Get the max id of user to autoincrement.
    *
    * @return Int of max id
    */
  private def getIdNum(requestTable: Boolean = false): Int = {
    val query = MongoDBObject() // All documents
    val fields = MongoDBObject("_id" -> 1) // Only return `_id`
    val orderBy = MongoDBObject("_id" -> -1) // Order by _id descending
    var item : Option[TypeImports.DBObject] = None

    if(requestTable){
      item = collectionRequest findOne(query, fields, orderBy)
    } else {
      item = collection findOne(query, fields, orderBy)
    }

    if(item.isEmpty){
      return 0
    }

    val id = item.get.as[Int]("_id")

    id + 1
  }

  private def getCodes(): List[Int] = {
    val res = collection.find()

    val returnlist = new ListBuffer[Int]
    for(x <- res) {
      returnlist += x.getAs[Int]("code").get
    }

    returnlist.toList
  }

  /**
    * Delete customer.
    *
    * @param id
    */
  override def deleteCustomer(id: Int): Unit = {
    val query = MongoDBObject("_id" -> id)
    collection.remove(query)
  }

  /**
    * Get customers list.
    *
    * @return list of cusomters.
    */
  override def getCustomers(): List[Customer] = {
    val res = collection.find()

    val result = new ListBuffer[Customer]()

    for(x <- res){
      result += getCustomer(x)
    }

    result.toList.sortWith((a, b) => if (a.name < b.name) true; else false)
  }

  /**
    * Return list of customer by code.
    *
    * @param code
    * @return
    */
  override def getCustomerListByCode(code: Int): List[Customer] = {
    val query = MongoDBObject("code" -> code)

    val li = collection.find(query)
    val result = new ListBuffer[Customer]()

    for(x <- li){
      result += getCustomer(x)
    }

    result.toList.sortWith((a, b) => if (a.name < b.name) true; else false)
  }

  /**
    * Insert a new customer to request table.
    *
    * @param name    name of customer
    * @param add     address of customer
    * @param payment payment of customer
    * @return success
    */
  override def insertRequest(name: String, add: String, payment: String): Boolean = {

    val id = getIdNum(true)

    val newCustomer = MongoDBObject (
      "_id" -> id,
      "code" -> 0,
      "name" -> name,
      "lowerName" -> name.toLowerCase,
      "billing_name" -> name,
      "p_type" -> payment,
      "billing_address" -> add
    )

    collectionRequest.insert(newCustomer)

    true
  }
}
