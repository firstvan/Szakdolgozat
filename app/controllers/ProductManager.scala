package controllers

import controllers.db.{ProductDAO, ActualOrderDAO}
import play.api.mvc.Controller


/**
  * Created by firstvan on 15/01/16.
  */
class ProductManager extends Controller with Secured{

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

  def index = withUser("admin") { username => implicit request =>
    Ok(views.html.ProductModify(username.username, closed = true))
  }

  def modifyIndex(id: Int) = withUser("admin") {username => implicit request =>
    var product: model.Product = null
    if(id != 0){
      product = ProductDAO.getProductByItemNo(id.toString).get
    }
    Ok(views.html.ProductModifyForm(username.username, product))
  }

  def updateProduct = withUser("admin") {username => implicit request =>
    var ok = 0

    val map : Map[String,Seq[String]] = request.body.asFormUrlEncoded.getOrElse(Map())

    val id: Seq[String] = map.getOrElse("id", List[String]())
    val prodnum: Seq[String] = map.getOrElse("prodnum", List[String]())
    val ean: Seq[String] = map.getOrElse("ean", List[String]())
    val name: Seq[String] = map.getOrElse("name", List[String]())
    val price: Seq[String] = map.getOrElse("price", List[String]())
    val stock: Seq[String] = map.getOrElse("stock", List[String]())

    if(id.head.toInt == 0){
      ok = ProductDAO.insertItem(prodnum.head, ean.head, name.head, price.head.toInt, stock.head.toInt)
    } else {
      ProductDAO.updateItem(prodnum.head, ean.head, name.head, price.head.toInt, stock.head.toInt)
    }

    Ok(ok.toString)
  }
}
