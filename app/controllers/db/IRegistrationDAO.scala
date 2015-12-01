package controllers.db

import model.Registration

trait IRegistrationDAO {
  def getRegistrationByCustomerId(id: Int): Option[List[Registration]]

  def getRegistrationByUser(userId: Int, opened :Int=3): Option[List[Registration]]
}
