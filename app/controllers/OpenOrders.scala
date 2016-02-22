package controllers

import controllers.db._
import org.joda.time.format.DateTimeFormat
import play.api.mvc.{Cookie, Controller}


class OpenOrders extends Controller with Secured{

  def getInfromation(id: Int) = withAuth { username => implicit request =>
    val order = RegistrationDAO.getOrderById(id)
    val cust = CustomerDAO.getCustomerById(order.customer)
    val f = DateTimeFormat.forPattern("yyyy.MM.dd")
    val date  = f.print(order.date_of_take)
    val delevery = f.print(order.delivery_date)
    Ok(views.html.openinform(username, order, cust.get.name, date, delevery)).withCookies(new Cookie("orderid", id.toString))
  }

  def deleteOrder(id: Int) = withAuth { username => implicit request =>
    ActualOrderDAO.deleteOrder(id)
    Ok("1")
  }

  def getTotal = withAuth { username => implicit request =>

    var order_id = 0
    val order_id_cookie = request.cookies.get("orderid")

    if (order_id_cookie.isDefined) {
      order_id = order_id_cookie.get.value.toInt
    }

    val total = ActualOrderDAO.getTotal(order_id)
    Ok(total.toString)
  }

  def updateTime(time: String) = withAuth { username => implicit request =>
    var order_id = 0
    val order_id_cookie = request.cookies.get("orderid")

    if (order_id_cookie.isDefined) {
      order_id = order_id_cookie.get.value.toInt
    }

    val updated = ActualOrderDAO.updateTime(order_id, time)

    Ok(updated.toString)
  }

  /**
    * Close order by id.
 *
    * @param id order_id
    * @return success
    */
  def closeOrder(id: Int) = withAuth { username => implicit  request =>
      val res = ActualOrderDAO.closeOrder(id: Int)
    println(res.toString)
      Ok(res.toString)
  }

  /**
    * Show the order to close.
 *
    * @param id Order id
    * @return
    */
  def orderToClose(id: Int) = withAuth { username => implicit request =>
    val order = RegistrationDAO.getOrderById(id)
    val cust = CustomerDAO.getCustomerById(order.customer)
    val f = DateTimeFormat.forPattern("yyyy.MM.dd")
    val date  = f.print(order.date_of_take)
    val delevery = f.print(order.delivery_date)
    val ktar = UserDAO.getUserByUserID(order.sales_man_id)
    Ok(views.html.adminProducts(username, order, cust.get.name, date, delevery, ktar.get.fullname)).withCookies(new Cookie("orderid", id.toString))
  }

  /**
    * Get product for delivery-
 *
    * @param id productnumber
    * @param db orderd product
    * @return success
    */
  def closeItem(id: String, db: Int) = withAuth { username => implicit request =>
    var order_id = 0
    val order_id_cookie = request.cookies.get("orderid")

    if (order_id_cookie.isDefined) {
      order_id = order_id_cookie.get.value.toInt
    }

    val res = ActualOrderDAO.closeItem(order_id, id, db)

    Ok(res._1.toString+ " " + res._2.toString)
  }

  /**
    * Return the delivered total.
 *
    * @return Respons which contains delivery total.
    */
  def getDTotal = withAuth { username => implicit request =>

    var order_id = 0
    val order_id_cookie = request.cookies.get("orderid")

    if (order_id_cookie.isDefined) {
      order_id = order_id_cookie.get.value.toInt
    }

    val total = ActualOrderDAO.getDeliveryTotal(order_id)
    Ok(total.toString)
  }
}