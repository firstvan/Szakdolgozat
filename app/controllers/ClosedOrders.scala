package controllers

import controllers.db.{UserDAO, RegistrationDAO, CustomerDAO}
import model.{TableLook, Orders}
import org.joda.time.format.DateTimeFormat
import play.api.mvc.{Cookie, Controller}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by Firstvan on 2016. 02. 24..
  */
class ClosedOrders extends Controller with Secured {

  def index(all: Boolean) = withAuth { username => implicit request =>
    var menu = 7
    if(!all){
      menu = 2
    }

    Ok(views.html.ClosedOrder(username, menu))
  }

  def getTable = withAuth{ username => implicit request =>
    val usr = UserDAO.getUserByUserName(username)

    var id = 0
    if(usr.get.accountType.equals("Manager")){
      id = usr.get._id
    }

    val name_cookie = request.cookies.get("search_name")
    var name = ""
    if (name_cookie.isDefined) {
      name = name_cookie.get.value
    }

    val start_cookie = request.cookies.get("start_date")
    var start = ""
    if (start_cookie.isDefined) {
      start = start_cookie.get.value
    }

    val end_cookie = request.cookies.get("end_date")
    var end = ""
    if (end_cookie.isDefined) {
      end = end_cookie.get.value
    }

    val list = tableLook(RegistrationDAO.getOrders(name, start, end, id))

    Ok(views.html.ClosedOrderTable(list))
  }

  def getInfromation(id: Int) = withAuth { username => implicit request =>
    val usr = UserDAO.getUserByUserName(username)
    var menu = 6
    if(usr.get.accountType.equals("Manager")){
      menu = 2
    }

    val order = RegistrationDAO.getOrderById(id)
    val cust = CustomerDAO.getCustomerById(order.customer)
    val f = DateTimeFormat.forPattern("yyyy.MM.dd")
    val date  = f.print(order.date_of_take)
    val delevery = f.print(order.delivery_date)
    val ktar = UserDAO.getUserByUserID(order.sales_man_id)
    Ok(views.html.adminProducts(username, order, cust.get.name, date, delevery, ktar.get.fullname, closed = true, menu)).withCookies(new Cookie("orderid", id.toString))
  }

  private def tableLook(li: List[Orders]) : List[TableLook] ={
    val map = mutable.HashMap[Int, String]()
    val returnList = ListBuffer[TableLook]()
    for(i <- li){

      var name = ""

      if(map.contains(i.customer)){
        name = map.get(i.customer).get
      } else {
        name = CustomerDAO.getCustomerById(i.customer).get.name
        map += ((i.customer, name))
      }

      val f = DateTimeFormat.forPattern("yyyy.MM.dd")
      val date  = f.print(i.date_of_take)

      returnList.append(new TableLook(i._id, name, date, i.total))
    }

    returnList.toList
  }
}
