package controllers.db

import com.mongodb.casbah.Imports._
import model.Product

import scala.collection.mutable.ListBuffer

object ProductDAO extends IProductDAO {
  val collection = DBFactory.getCollection("products")

  override def getProductByItemNo(itemNo: String): Option[Product] = {
    val itemObj = collection.findOne(MongoDBObject("productnumber" -> itemNo))

    if (itemObj.isEmpty) {
      return None
    }

    val item = itemObj.get

    Some(getProduct(item))
  }

  override def getElementByName(name: String): List[Product] = {
    val nameWithRegex = "^" + name.toLowerCase() + ".*"

    val productsObj = collection.find(MongoDBObject("lowerName" -> nameWithRegex.r))
    val return_list = new ListBuffer[Product]()

    for (x <- productsObj) {
      return_list += getProduct(x)
    }

    return_list.toList.sortWith((a, b) => if (a.name < b.name) true; else false)
  }

  private def getProduct(item: ProductDAO.this.collection.T): Product = {
    new Product(item.getAs[String]("productnumber").get,
      item.getAs[String]("name").get,
      item.getAs[Int]("ar").get,
      item.getAs[Int]("stock").get,
      item.getAs[String]("ean").get,
      item.getAs[String]("lowerName").get
    )
  }

  /**
    * Update stock of product.
    *
    * @param productNumber
    * @param piece
    * @return return updated stock number
    */
  override def updateStock(productNumber: String, piece: Int, sub: Int): Int = {
    val query = MongoDBObject("productnumber" -> productNumber)

    val res = collection.findOne(query)

    val stock = res.get.getAs[Int]("stock").get
    if (stock >= sub) {
        collection.findAndModify(query, $set("stock" -> (stock - sub)))
        piece
    } else {
      if (sub < 0) {
        collection.findAndModify(query, $set("stock" -> (stock - sub)))
        piece
      }
      else {
        collection.findAndModify(query, $set("stock" -> 0))
        sub - stock
      }
    }
  }

}
