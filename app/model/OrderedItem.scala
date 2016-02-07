package model

case class OrderedItem(_id: Int, order_id: Int, product_number: String, ordered_piece: Int, ordered_price: Int, delivered: Int)
{
  override def equals(o: Any) = o match {
    case that: OrderedItem => that.product_number.equals(product_number)
    case _ => false
  }
}