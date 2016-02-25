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

  /**
    * Update product by params
    * @param productNumber
    * @param ean
    * @param name
    * @param price
    * @param stock
    */
  def updateItem(productNumber: String, ean: String, name: String, price: Int, stock: Int) : Unit

  /**
    * Insert a product by params
    * @param productNumber
    * @param ean
    * @param name
    * @param price
    * @param stock
    * @return
    */
  def insertItem(productNumber: String, ean: String, name: String, price: Int, stock: Int): Int
}
