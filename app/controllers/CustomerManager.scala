package controllers

import controllers.db.{UserDAO, ProductDAO, CustomerDAO}
import model.Customer
import play.api.mvc.Controller

import scala.collection.mutable.ListBuffer

class CustomerManager extends Controller with Secured {

  def index = withAuth { username => implicit request =>
    val list = getCustomList("", 1, 10)
    Ok(views.html.CustomersManager(username, list._1))
  }

  def modifyCustomer(id: Int) = withAuth{ username => implicit request =>
    val tUser = UserDAO.getUserByUserName(username).get.accountType.equals("Manager")

    if(id == 0){
      if(tUser) {
        Ok(views.html.CustomerModify(username, null, tUser, 10))
      } else {
        Ok(views.html.CustomerModify(username, null, tUser))
      }
    } else {
      val cust = CustomerDAO.getCustomerById(id)

      Ok(views.html.CustomerModify(username, cust.get, manager = false))
    }
  }

  def saveCustomer = withAuth { username => implicit request =>
    val map : Map[String, Seq[String]] = request.body.asFormUrlEncoded.getOrElse(Map())

    val id: Seq[String] = map.getOrElse("id", List[String]())
    val name: Seq[String] = map.getOrElse("name", List[String]())
    val addr: Seq[String] = map.getOrElse("addr", List[String]())
    val payment: Seq[String] = map.getOrElse("payment", List[String]())
    var retid = id.head
    if(id.head.toInt == 0) {
      val succ = CustomerDAO.insertCustomer(name.head, addr.head, payment.head)
      if(!succ){
        retid = "-1"
      }
    } else {
        CustomerDAO.saveCustomer(id.head.toInt, name.head, addr.head, payment.head)
    }

    Ok(retid)
  }

  def customerTable = withAuth {username => implicit request =>
    val actual_page_cookie = request.cookies.get("actual_page")
    var actual_page = 1
    if (actual_page_cookie.isDefined) {
      actual_page = actual_page_cookie.get.value.toInt
    }

    val search_name_cookie = request.cookies.get("search_name")
    var search_name = ""
    if (search_name_cookie.isDefined) {
      search_name = search_name_cookie.get.value
    }

    val list = getCustomList(search_name, actual_page, 10)

    Ok(views.html.CustomerTable(list._1, 10, actual_page, list._2))
  }

  def deleteCustomer(id: Int) = withAuth { username => implicit request =>

    CustomerDAO.deleteCustomer(id)

    Ok("0")
  }

  private def getCustomList(name: String, start: Int, size: Int) : (List[Customer], Int) = {
    var p2 = new ListBuffer[Customer]()

    val custList = CustomerDAO.getCustomerList(name)

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

  def getCustomersByName(name: String) = withAuth {username => implicit request =>
    val list = CustomerDAO.getCustomerList(name)

    Ok(views.html.addOrderUserTable(list))
  }

  def getCustomersByCode(code: String) = withAuth {username => implicit request =>
    var list :List[Customer] = null
    if(code.isEmpty){
      list = CustomerDAO.getCustomerList("")
    } else {
      list = CustomerDAO.getCustomerListByCode(code.toInt)
    }

    Ok(views.html.addOrderUserTable(list))
  }
}
