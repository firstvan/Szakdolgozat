/**
  * Created by Firstvan on 2016. 02. 11..
  */
package model

object OrderState extends Enumeration {
  type OrderState = Value
  val Open = 0
  val Closed = 1
}