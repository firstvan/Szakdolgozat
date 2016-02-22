package controllers.db

import com.mongodb.casbah.commons.MongoDBObject
import model.Customer
import com.mongodb.casbah.Imports._

import scala.collection.mutable.ListBuffer

object CustomerDAO extends ICustomerDAO{
  val collection = DBFactory.getCollection("customers")

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

    return return_list.toList
  }

  override def getLowerCustomerNames(): List[String] = {
    val cust = collection.find()

    val return_list = ListBuffer[String]()

    for(c <- cust){
      return_list.append(c.getAs[String]("lowerName").get)
    }

    return return_list.toList
  }

  private def getCustomer(item: collection.T) : Customer = {
    return new Customer(item.getAs[Int]("_id").get, item.getAs[String]("name").get,
      item.getAs[String]("billing_name").get, item.getAs[String]("billing_address").get,
      item.getAs[String]("p_type").get, item.getAs[String]("lowerName").get)
  }

  override def getCustomerByName(name: String): Option[Customer] = {
    val cust = collection.findOne(MongoDBObject("name" -> name))

    if(cust.isEmpty){
      return None
    }

    return Some(getCustomer(cust.get))
  }
}
