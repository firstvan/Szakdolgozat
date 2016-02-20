package controllers

import controllers.db.{ActualOrderDAO, CustomerDAO, RegistrationDAO}
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
}