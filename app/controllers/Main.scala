package controllers


import model.{TableLook, Registration, Product}
import org.joda.time.format.DateTimeFormat
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._

import controllers.db.{ActualOrderDAO, ProductDAO, CustomerDAO, RegistrationDAO}
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

object Main {


  def tableLook(li: List[Registration]) : List[TableLook] ={
    val map = HashMap[Int, String]()
    val returnList = ListBuffer[TableLook]()
    for(i <- li){
      var name = ""
      if(map.contains(i.cust_no)){
        name = map.get(i.cust_no).get
      } else {
        name = CustomerDAO.getCustomerById(i.cust_no).get.name
        map += ((i.cust_no, name))
      }
      val f = DateTimeFormat.forPattern("yyyy.MM.dd")
      val date  = f.print(i.incomeDate)
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
    val l = RegistrationDAO.getRegistrationByUser(4, 1)
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
        ActualOrderDAO.addNewOrder(username, customer._1 , customer._2)
        Redirect(routes.Main.products())
          .withCookies(new Cookie("customername", name))
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
    else
      Ok(views.html.products(username, name.get.value.replace("%", " "), products._1, DefaultValues.Size,
        DefaultValues.ActualPage, DefaultValues.MaxPageNumber, products._2)).withCookies(new Cookie("searched", DefaultValues.Searched),
          new Cookie("listedSize", DefaultValues.Size.toString()))
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

    //println(p2.toList.length)
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

    Ok(views.html.products(username, name.get.value.replace("%", " "), products._1, size, page, DefaultValues.MaxPageNumber, products._2))
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
        Ok(views.html.products(username, name1.get.value.replace("%", " "),  products._1, size, DefaultValues.ActualPage, DefaultValues.MaxPageNumber, products._2)).withCookies(new Cookie("searched", name.name))
      }
    )
  }
}
