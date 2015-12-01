package controllers.db

import com.mongodb.casbah.{MongoCollection, MongoClient}


object DBFactory {
  val mongoClient = MongoClient()

  val dbname = "uk"

  def getCollection(name: String) : MongoCollection = {
    return mongoClient(dbname)(name)
  }
}
