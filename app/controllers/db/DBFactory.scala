package controllers.db

import com.mongodb.casbah.commons.{Imports, MongoDBObject}
import com.mongodb.casbah.{MongoCollection, MongoClient}


object DBFactory {
  val mongoClient = MongoClient()

  val dbname = "uk"

  def getCollection(name: String) : MongoCollection = {
    return mongoClient(dbname)(name)
  }

  def buildMongoDbObject(obj: AnyRef) : Imports.DBObject = {
    val map = getCCParams(obj)
    val mongoObejct = MongoDBObject.newBuilder

    map.foreach {
      keyVal => mongoObejct += keyVal._1 -> keyVal._2
    }

    val result = mongoObejct.result()
    println(result.toString())

    return result
  }

  def getCCParams(cc: AnyRef) =
    (Map[String, Any]() /: cc.getClass.getDeclaredFields) {(a, f) =>
      f.setAccessible(true)
      a + (f.getName -> f.get(cc))
    }
}
