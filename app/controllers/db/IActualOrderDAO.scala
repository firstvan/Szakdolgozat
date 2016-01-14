package controllers.db

trait IActualOrderDAO {
def addOrder(productNumber: String ,ordered: Int, actualPrice: Int)
}
