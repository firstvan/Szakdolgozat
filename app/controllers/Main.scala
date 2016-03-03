package controllers


import controllers.db._
import model.OrderState
import model._
import org.joda.time.format.DateTimeFormat
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Main {

  var user: User = null

  def tableLook(li: List[Orders]) : List[TableLook] ={
    val map = mutable.HashMap[Int, String]()
    val returnList = ListBuffer[TableLook]()
    for(i <- li){
      var name = ""
      var found  = false
      if(map.contains(i.customer)){
        name = map.get(i.customer).get
      } else {
        val usr = CustomerDAO.getCustomerById(i.customer)
        if(usr.isDefined){
          found = true
          name = usr.get.name
          map += ((i.customer, name))
        }
      }
      if(found) {
        val f = DateTimeFormat.forPattern("yyyy.MM.dd")
        val date = f.print(i.date_of_take)
        returnList.append(new TableLook(i._id, name, date, i.total))
      }
    }

    returnList.toList
  }
}

class Main extends Controller with Secured{

  def index = withAuth { username => implicit request =>
    if(Main.user == null){
      Main.user =  UserDAO.getUserByUserName(username).get
    }

    if(Main.user.accountType.equals("admin")){
      Ok(views.html.mainAdmin(username))
    } else {
      val l = RegistrationDAO.getRegistrationByUser(Main.user._id, OrderState.Open)
      if (l.isDefined) {
        val li = Main.tableLook(l.get)
        Ok(views.html.mainpage(username, li))
      }
      else
        Ok(views.html.mainpage(username, List[TableLook]()))
    }
  }

  def addorder = withUser("Manager") { user => implicit request =>
    val custnames = CustomerDAO.getCustomers()

    Ok(views.html.addorder(user.username, custnames))
  }


  def add = withUser("Manager")  { user => implicit request =>
    val map: Map[String, Seq[String]] = request.body.asFormUrlEncoded.getOrElse(Map())

    val code: Seq[String] = map.getOrElse("code", List[String]())
    val date: Seq[String] = map.getOrElse("date", List[String]())

    val orderID = ActualOrderDAO.addNewOrder(user.username, code.head, date.head)

    Ok("/products").withCookies(new Cookie("custcode", code.head)
      , new Cookie("orderid", orderID.toString))
  }

  def products = withAuth { username => implicit request =>
    val cust_code = request.cookies.get("custcode").get.value
    val cust_name = CustomerDAO.getCustomerListByCode(cust_code.toInt)
    Ok(views.html.products(username, cust_name.head.name ))
  }

  def adminIndex = withUser("admin")  { username => implicit request =>
    val l = RegistrationDAO.getRegistrationByUser(0, OrderState.Open)
    if (l.isDefined) {
      val li = Main.tableLook(l.get)
      Ok(views.html.adminTable(li))
    }
    else
      Ok(views.html.adminTable(List[TableLook]()))
  }
}
