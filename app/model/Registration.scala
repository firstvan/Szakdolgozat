package model

import org.joda.time.DateTime

case class Item(productnumber: String, ordered: Int, price: Int)

case class Registration(_id: Int, user_no: Int, cust_no: Int, products: List[Item], total: Int, opened: Int, incomeDate: DateTime, shippingDate: DateTime)
