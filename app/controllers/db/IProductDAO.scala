package controllers.db

import model.Product

trait IProductDAO {
  def getProductByItemNo(itemNo: String) : Option[Product]

  def getElementByName(name: String) : List[Product]

  /**
    * This method return a list which size is in size argument, and start from start argument, and contains only
    * products which starts with name argument.
    * @param name
    * @param start
    * @param size
    * @return
    */
  def getElementByName(name: String, start: Int, size: Int) : List[Product]

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
