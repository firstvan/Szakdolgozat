package controllers

import controllers.db.ActualOrderDAO
import play.api.mvc.Controller


/**
  * Created by firstvan on 15/01/16.
  */
class ProductManage extends Controller with Secured{

  /**
    * Gateway to jquery and database.
    *
    * @param itemNo product number
    * @param piece priece of product
    * @param price actual price of product
    * @return success information
    */
  def addProduct(itemNo :String, piece :Int, price :Int) = withAuth{ username => implicit request =>

    val ord_id = request.cookies.get("orderid")

    if(ord_id.isEmpty){
      Ok("1")
    }

    ActualOrderDAO.addOrder(ord_id.get.value.toInt, itemNo, piece, price)

    Ok("0")
  }


}
