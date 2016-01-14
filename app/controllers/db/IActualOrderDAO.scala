package controllers.db

trait IActualOrderDAO {
def addOrder(orderNumber: Int, productNumber: String ,ordered: Int, actualPrice: Int)


  /**
    * This method will add a new record to orders table.
    */
  def addNewOrder(salesManUserName: String, customerId : String, delivery: String) : Int
}
