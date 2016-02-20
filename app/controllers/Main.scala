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

class Main extends Controller with Secured{

  val newOrder = Form(
    tuple(
      "custName" -> text,
      "shippingDate" -> text
    )
  )

  def index = withAuth { username => implicit request =>
    if(Main.user == null){
      Main.user =  UserDAO.getUserByUserName(username).get
    }

    val l = RegistrationDAO.getRegistrationByUser(Main.user._id, OrderState.Open)
    if(l.isDefined) {
      val li = Main.tableLook(l.get)
      Ok(views.html.mainpage(username, li))
    }
    else
      Ok(views.html.mainpage(username, List[TableLook]()))
  }

  def addorder = withAuth { username => implicit request =>
    val custnames = CustomerDAO.getCustomerNames()
    Ok(views.html.addorder(username, custnames))
  }


  def add = withAuth { username => implicit request =>
    newOrder.bindFromRequest.fold(
      formWithErrors => BadRequest("Formális hiba"),
      customer => {
        val orderID = ActualOrderDAO.addNewOrder(username, customer._1 , customer._2)

        Redirect(routes.Main.products).withCookies(new Cookie("customername", customer._1.replace(" ", "%"))
          , new Cookie("orderid", orderID.toString))
      }
    )
  }

  def products = withAuth { username => implicit request =>
    val cust_name = request.cookies.get("customername").get.value

    Ok(views.html.products(username, cust_name.replace("%", " ")))
  }

}
