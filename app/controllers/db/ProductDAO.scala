package controllers.db

import com.mongodb.casbah.Imports._
import model.Product

import scala.collection.mutable.ListBuffer

object ProductDAO extends IProductDAO{
  val collection = DBFactory.getCollection("products")

  override def getProductByItemNo(itemNo: String): Option[Product] = {
    val itemObj = collection.findOne(MongoDBObject("productnumber" -> itemNo))

    if(itemObj.isEmpty){
      return None
    }

    val item = itemObj.get

    return Some(getProduct(item))
  }

  override def getElementByName(name: String): List[Product] = {
    val nameWithRegex = "^" + name.toLowerCase()+".*"

    val productsObj = collection.find(MongoDBObject("lowerName"-> nameWithRegex.r))
    val return_list = new ListBuffer[Product]()

    for(x <- productsObj){
      return_list += getProduct(x)
    }

    return return_list.toList.sortWith((a, b) => if(a.name < b.name) true; else false)
  }

  private def getProduct(item: ProductDAO.this.collection.T) : Product = {
    return new Product(item.getAs[String]("productnumber").get,
                       item.getAs[String]("name").get,
                       item.getAs[Int]("ar").get,
                       item.getAs[Int]("stock").get,
                       item.getAs[String]("ean").get,
                       item.getAs[String]("lowerName").get
                      )
  }

}
