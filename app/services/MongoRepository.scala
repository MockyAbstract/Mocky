package services

import scala.concurrent.Future
import org.slf4j.LoggerFactory

import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.DefaultBSONHandlers._
import reactivemongo.api.collections.default.BSONCollection

import play.modules.reactivemongo._
import play.modules.reactivemongo.json.ImplicitBSONHandlers._
import play.modules.reactivemongo.json.BSONFormats._

import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.Play.current

import models.{Metadata, Mocker, MockResponse}
import models.MockResponse._
import com.ning.http.util.Base64

object MongoRepository extends IRepository {

  implicit val logger = LoggerFactory.getLogger("ws.mongo")

  val db = ReactiveMongoPlugin.db
  lazy val collection = db[BSONCollection]("mocks")

  val addMongoId =  __.json.update((__ \ '_id).json.put(Json.toJson(BSONObjectID.generate)))

  def getMockFromId(id: String): Future[MockResponse] = {
    val q = Json.obj("_id" -> Json.obj("$oid" -> id))
    collection.find[JsValue](q).one.map {
      case Some(obj) => {
        (obj: JsValue).validate[MockResponse]
          .getOrElse(MongoDbException(s"Cannot convert entity $id"))
      }
      case _ => MongoDbException(s"Mock $id not found")
    }
  }

  def save(mock: Mocker): Future[String] = {
    val metadata = Metadata(mock.status, mock.charset, mock.headers, Repository.version)
    val mockResponse = MockResponse(encodeBody(mock.body), metadata)

    Json.toJson(mockResponse).transform(addMongoId).map { jsobj =>

      collection.insert[JsValue](jsobj).map{ p =>
        if (p.inError)
          MongoDbException(s"Cannot insert Mock in DB ($p)")
        else
          jsobj.transform((__ \ '_id).json.pick).map(_.as[BSONObjectID].stringify)
            .getOrElse(MongoDbException(s"Cannot retrieve inserted Mock in db ($p)"))
      }
    }.recoverTotal {
      case e => MongoDbException(s"Cannot transform mock object ($e)")
    }
  }

  // TODO: Handle better charset!
  def decodeBody(content: String, charset: String) = content
  def encodeBody(content: String, charset: String) = content

  object MongoDbException {
    def apply(msg: String) = {
      logger.error(msg)
      throw new RuntimeException(msg)
    }
  }
}
