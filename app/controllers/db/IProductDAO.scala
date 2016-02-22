package controllers.db

import model.Product

trait IProductDAO {
  def getProductByItemNo(itemNo: String) : Option[Product]

  def getElementByName(name: String) : List[Product]

  /**
    * Update stock of product.
    * @param productNumber
    * @param piece
    * @return return updated stock number
    */
  def updateStock(productNumber: String, piece: Int, sub: Int): Int
}
