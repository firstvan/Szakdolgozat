package controllers

import controllers.db.{RegistrationDAO, CustomerDAO}
import model.{TableLook, Orders}
import org.joda.time.format.DateTimeFormat
import play.api.mvc.Controller

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by Firstvan on 2016. 02. 24..
  */
class ClosedOrders extends Controller with Secured {

  def index(all: Boolean) = withAuth { username => implicit request =>

    Ok(views.html.ClosedOrder(username))
  }

  def getTable = withAuth{ username => implicit request =>
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

    val list = tableLook(RegistrationDAO.getOrders(name, start, end))

    Ok(views.html.ClosedOrderTable(list))
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