package controllers

import controllers.db.{CustomerDAO, RegistrationDAO}
import org.joda.time.format.DateTimeFormat
import play.api.mvc.Controller

class OpenOrders extends Controller with Secured{

  def getInfromation(id: Int) = withAuth { username => implicit request =>
    val order = RegistrationDAO.getOrderById(id)
    val cust = CustomerDAO.getCustomerById(order.customer)
    val f = DateTimeFormat.forPattern("yyyy.MM.dd")
    val date  = f.print(order.date_of_take)
    val delevery = f.print(order.delivery_date)
    Ok(views.html.openinform(username, order, cust.get.name, date, delevery))
  }
}
