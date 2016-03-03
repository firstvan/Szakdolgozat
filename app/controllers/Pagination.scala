package controllers

import controllers.db.{ActualOrderDAO, ProductDAO}
import model.Product
import play.api.mvc.Controller

import scala.collection.mutable.ListBuffer

/**
  * Created by Firstvan on 2016. 02. 12..
  */

class Pagination extends Controller with Secured {


  def index(orderd: Boolean, admin: Boolean = false, closed :Boolean = false, modify: Boolean = false) = withAuth { username => implicit request =>
    val actual_page_cookie = request.cookies.get("actual_page")
    var actual_page = 1
    if (actual_page_cookie.isDefined) {
      actual_page = actual_page_cookie.get.value.toInt
    }


    val name_cookie = request.cookies.get("search_item_name")
    var name = ""
    if (name_cookie.isDefined) {
      name = name_cookie.get.value
    }

    val size_cookie = request.cookies.get("list_size")
    var size = 5
    if (size_cookie.isDefined) {
      size = size_cookie.get.value.toInt
    }

    val max_page_number_cookie = request.cookies.get("maxpagenumber")
    var max_page_number = 8
    if (max_page_number_cookie.isDefined) {
      max_page_number = max_page_number_cookie.get.value.toInt
    }

    var order_id = 0
    val order_id_cookie = request.cookies.get("orderid")

    if (order_id_cookie.isDefined) {
      order_id = order_id_cookie.get.value.toInt
    }

    var li : (List[Product], Int) = (null, 1)

    if(orderd){
      li = getListOrderd(name, size, actual_page, order_id)
    } else {
      li = getList(name, size, actual_page)
    }


    if(admin){
      val map = ActualOrderDAO.getOrderedAndDeliveriedProducts(order_id)
      Ok(views.html.adminpaging(li._1, map, actual_page, size, max_page_number, li._2, closed))
    } else {
      var map = Map[String, Int]()
      println(order_id)
      if(order_id != 0) {
        map = ActualOrderDAO.getOrderedProducts(order_id)
      }
      Ok(views.html.productlisting(li._1, map, actual_page, size, max_page_number, li._2, modify))
    }
  }

  def test = withAuth { username => implicit request =>
    Ok(views.html.test(username, "hello"))
  }

  def getList(name: String, size: Int, start: Int):
  (List[Product], Int) = {
    var p2 = new ListBuffer[model.Product]()

    val products = ProductDAO.getElementByName(name)

    var counter = (start - 1) * size
    var max = counter + size

    if (max > products.size) {
      max = products.size
    }

    while (counter < max) {
      p2 += products(counter)
      counter += 1
    }

    (p2.toList, products.size)
  }

  def getListOrderd(name:String, size: Int, start: Int, order_id: Int): (List[Product], Int)  ={
    var p2 = new ListBuffer[model.Product]()

    val products = getOrderdList(ProductDAO.getElementByName(name), order_id)
    var counter = (start - 1) * size
    var max = counter + size

    if (max > products.size) {
      max = products.size
    }

    while (counter < max) {
      p2 += products(counter)
      counter += 1
    }

    (p2.toList, products.size)
  }

  def getOrderdList(orderd: List[Product], order_id: Int) : List[Product] = {
    var p2 = new ListBuffer[model.Product]()
    val map = ActualOrderDAO.getOrderedProducts(order_id)
    for(o <- orderd){
      if(map.contains(o.productnumber)){
        p2 += o
      }
    }

    p2.toList
  }

  def deleteItem(prodNo: String) = withUser("Manager"){username=> implicit request =>
    var order_id = 0
    val order_id_cookie = request.cookies.get("orderid")

    if (order_id_cookie.isDefined) {
      order_id = order_id_cookie.get.value.toInt
    }

    val succes = ActualOrderDAO.deleteItem(order_id, prodNo)
    Ok(succes.toString)
  }
}