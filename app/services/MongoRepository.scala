package services

import scala.concurrent.Future
import scala.util.control.NonFatal

import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection.{JSONCollection, _}
import org.slf4j.LoggerFactory

import models.MockResponse._
import models.{Metadata, MockResponse, Mocker}
import reactivemongo.api._
import reactivemongo.bson.BSONObjectID

object MongoRepository extends IRepository {

  private val logger = LoggerFactory.getLogger("ws.mongo")

  private lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi] // Oh yearhhh
  private lazy val collection = reactiveMongoApi.db[JSONCollection]("mocks")

  val addMongoIdTransformer =  __.json.update((__ \ '_id).json.put(Json.toJson(BSONObjectID.generate)))

  def getMockFromId(id: String): Future[MockResponse] = {
    val query = Json.obj("_id" -> Json.obj("$oid" -> id))
    val cursor: Cursor[MockResponse] = collection.find(query).cursor[MockResponse](ReadPreference.nearest)
    val mocksList: Future[Option[MockResponse]] = cursor.headOption

    mocksList.flatMap {
      case Some(mock) => Future.successful(mock)
      case _ => Future.failed(new RuntimeException(s"Mock $id not found"))
    }
  }

  def save(mock: Mocker): Future[String] = {
    val metadata = Metadata(mock.status, mock.charset, mock.headers, Repository.version)
    val mockResponse = MockResponse(encodeBody(mock.body), metadata)

    Json.toJson(mockResponse)
      .transform(addMongoIdTransformer)
      .map { jsobj =>
        collection.insert[JsObject](jsobj).map { p =>
          if (p.inError)
            throw new RuntimeException(s"Cannot insert Mock in DB ($p)")
          else
            jsobj.transform((__ \ '_id).json.pick).map(_.as[BSONObjectID].stringify)
              .getOrElse(throw new RuntimeException(s"Cannot retrieve inserted Mock in db ($p)"))
        }.recoverWith {
          case NonFatal(ex) =>
            logger.error("Cannot save new mock into mongodb", ex)
            Future.failed(new RuntimeException("Cannot save mock"))
        }
    }.recoverTotal {
      case e => Future.failed(new RuntimeException(s"Cannot transform mock object ($e)"))
    }
  }

  // TODO: Handle better charset!
  def decodeBody(content: String, charset: String) = content
  def encodeBody(content: String, charset: String) = content

}
