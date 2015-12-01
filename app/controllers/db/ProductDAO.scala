package controllers.db

import com.mongodb.casbah.commons.MongoDBObject
import model.Product

class ProductDAO extends IProductDAO{
  val collection = DBFactory.getCollection("products")

  override def getProductByItemNo(itemNo: String): Option[Product] = {
    val itemObj = collection.findOne(MongoDBObject("productnumber" -> itemNo))

    if(itemObj.isEmpty){
      return None
    }

    val item = itemObj.get

    return Some(getProduct(item))
  }


  private def getProduct(item: ProductDAO.this.collection.T) : Product = {
    return new Product(item.get("productnumber").toString(), item.get("name").toString(),
      item.get("ar").toString().toInt, item.get("stock").toString().toInt, item.get("ean").toString(),
      item.get("lowerName").toString())
  }
}
