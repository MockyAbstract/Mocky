package services.repositories

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import org.slf4j.LoggerFactory
import reactivemongo.api._
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json._

import models.{Metadata, Mocker, MockResponse}
import models.MockResponse._

import services.IRepository

class MongoRepository(currentVersion: String, reactiveMongoApi: ReactiveMongoApi)(implicit ec: ExecutionContext) extends IRepository {

  private val logger = LoggerFactory.getLogger("ws.mongo")

  private lazy val collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("mocks"))

  private val addMongoIdTransformer =  __.json.update((__ \ '_id).json.put(Json.toJson(BSONObjectID.generate)))

  def getMockFromId(id: String): Future[MockResponse] = {
    val query = Json.obj("_id" -> Json.obj("$oid" -> id))
    val mocksList: Future[Option[MockResponse]] = collection.flatMap(_.find(query).cursor[MockResponse](ReadPreference.nearest).headOption)

    mocksList.flatMap {
      case Some(mock) => Future.successful(mock)
      case _ => Future.failed(new RuntimeException(s"Mock $id not found"))
    }
  }

  def save(mock: Mocker): Future[String] = {
    val metadata = Metadata(mock.status, mock.charset, mock.headers, currentVersion)
    val mockResponse = MockResponse(encodeBody(mock.body), metadata)

    Json.toJson(mockResponse)
      .transform(addMongoIdTransformer)
      .map { jsobj =>
        collection.flatMap(_.insert[JsObject](jsobj).map { p =>
          if (!p.ok)
            throw new RuntimeException(s"Cannot insert Mock in DB ($p)")
          else
            jsobj.transform((__ \ '_id).json.pick).map(_.as[BSONObjectID].stringify)
              .getOrElse(throw new RuntimeException(s"Cannot retrieve inserted Mock in db ($p)"))
        }.recoverWith {
          case NonFatal(ex) =>
            logger.error("Cannot save new mock into mongodb", ex)
            Future.failed(new RuntimeException("Cannot save mock"))
        })
    }.recoverTotal { e => Future.failed(new RuntimeException(s"Cannot transform mock object ($e)")) }
  }

  def decodeBody(content: String, charset: String): String = content

  def encodeBody(content: String, charset: String): String = content

}
