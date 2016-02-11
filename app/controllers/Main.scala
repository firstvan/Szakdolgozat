package controllers


import controllers.db._
import model.OrderState
import model._
import org.joda.time.format.DateTimeFormat
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._

import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

object Main {

  var user: User = null

  def tableLook(li: List[Orders]) : List[TableLook] ={
    val map = HashMap[Int, String]()
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

    return returnList.toList
  }
}

case class SearchName(name: String)

object DefaultValues {
  val Searched :String = ""
  val Size :Int = 5
  val ActualPage :Int = 1
  val MaxPageNumber :Int = 8
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
        val name = customer._1.replace(" ", "%")
        val orderID = ActualOrderDAO.addNewOrder(username, customer._1 , customer._2)
        //println(Main.orderID)
        Redirect(routes.Main.products())
          .withCookies(new Cookie("customername", name), new Cookie("orderid", orderID.toString()))
      }
    )
  }

  def products = withAuth {username => implicit request =>
    val products = getList(DefaultValues.Searched, DefaultValues.Size)
    val name = request.cookies.get("customername")

    if(name isEmpty)
    {
      BadRequest("Illetéktelen hozzáférés")
    }
    else {
      val orderedMap = ActualOrderDAO.getOrderedProducts(request.cookies.get("orderid").get.value.toInt)

      Ok(views.html.products(username, name.get.value.replace("%", " "), products._1, DefaultValues.Size,
        DefaultValues.ActualPage, DefaultValues.MaxPageNumber, products._2, orderedMap)).withCookies(new Cookie("searched", DefaultValues.Searched),
        new Cookie("listedSize", DefaultValues.Size.toString()))
    }
  }

  def getList(name :String = DefaultValues.Searched, size :Int = DefaultValues.Size, start :Int = DefaultValues.ActualPage) :
  (List[Product], Int) = {
    var p2 = new ListBuffer[model.Product]()

    val products = ProductDAO.getElementByName(name)

    var counter = (start - 1) * size
    var max = counter + size

    if(max > products.size){
      max = products.size
    }

    while(counter < max ){
      p2 += products(counter)
      counter += 1
    }

    return (p2.toList, products.size)
  }

  def paging(page :Int) = withAuth {username => implicit request =>
    val name = request.cookies.get("customername")

    if(name isEmpty)
    {
      BadRequest("Illetéktelen hozzáférés")
    }

    val sname = request.cookies.get("searched").get.value
    val size = request.cookies.get("listedSize").get.value.toInt
    val products = getList(sname, size, page)

    val orderedMap = ActualOrderDAO.getOrderedProducts(request.cookies.get("orderid").get.value.toInt)

    Ok(views.html.products(username, name.get.value.replace("%", " "), products._1, size, page, DefaultValues.MaxPageNumber, products._2, orderedMap))
  }

  val searchForm = Form(
    mapping(
      "name" -> text
    ) (SearchName.apply) (SearchName.unapply)
  )

  def search = withAuth { username => implicit request =>
    searchForm.bindFromRequest.fold(
      formWithError => Redirect("Not succes"),
      name => {
        val name1 = request.cookies.get("customername")

        if(name1 isEmpty)
        {
          BadRequest("Illetéktelen hozzáférés")
        }
        val size = request.cookies.get("listedSize").get.value.toInt
        val products = getList(name.name, size)

        val orderedMap = ActualOrderDAO.getOrderedProducts(request.cookies.get("orderid").get.value.toInt)

        Ok(views.html.products(username, name1.get.value.replace("%", " "),  products._1, size, DefaultValues.ActualPage, DefaultValues.MaxPageNumber, products._2, orderedMap)).withCookies(new Cookie("searched", name.name))
      }
    )
  }
}
