package controllers.db

import model.Product

trait IProductDAO {
  def getProductByItemNo(itemNo: String) : Option[Product]

}
