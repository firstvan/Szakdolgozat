package controllers

import controllers.db.{ProductDAO, CustomerDAO}
import model.Customer
import play.api.mvc.Controller

import scala.collection.mutable.ListBuffer

class CustomerManager extends Controller with Secured {

  def index = withAuth { username => implicit request =>
    val list = getCustomList("", 1, 10)
    Ok(views.html.CustomersManager(username, list._1))
  }

  private def getCustomList(name: String, start: Int, size: Int) : (List[Customer], Int) = {
    var p2 = new ListBuffer[Customer]()

    val custList = CustomerDAO.getCustomerList("")

    var counter = (start - 1) * size
    var max = counter + size

    if (max > custList.size) {
      max = custList.size
    }

    while (counter < max) {
      p2 += custList(counter)
      counter += 1
    }

    (p2.toList, custList.size)
  }
}
