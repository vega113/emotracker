package controllers

import javax.inject.Singleton

import org.slf4j.{LoggerFactory, Logger}
import play.api.mvc._

import scala.concurrent.Future

import play.api.Play.current

import anorm._
import play.api.db.DB
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import anorm.SqlParser._

/**
 * Created by vega on 6/6/15.
 */
@Singleton
class Emotions extends Controller {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Emotions])

  import models._
  import models.JsonFormats._

  def createEmotion = Action.async(parse.json) {
    request =>
      request.body.validate[Emotion].map {
        emotion =>
          Future {
            DB.withConnection { implicit conn =>
              SQL(
                """insert into Emotions (userId, emotion, reason, target, location, link)
                  | values ({userId}, {emotion}, {reason}, {target}, {location},  {link})"""
                  .stripMargin).
                on(emotion2NamedParams(emotion): _*).executeInsert()
            }
          }.flatMap(_ => Future.successful(Created(s"User Created")))

      }.getOrElse(Future.successful(BadRequest("invalid json"))).andThen {
        case _ => logger.error(s"invalid json: ${request.body}")
      }
  }


  lazy val p: ResultSetParser[List[(Option[Long], String, String, Option[String],
    Option[String], Option[String], Option[String])]] = {
    (get[Option[Long]]("id") ~
      str("userId") ~
      str("emotion") ~
      get[Option[String]]("reason") ~
      get[Option[String]]("target") ~
      get[Option[String]]("location") ~
      get[Option[String]]("link")).map(flatten).*
  }

  def findEmotions = Action.async {
    Future {
      DB.withConnection { implicit conn =>
        SQL("select * from Emotions").executeQuery().parse(p).
          map(x => Emotion(x._1, x._2, x._3, x._4, x._5, x._6, x._7))
      }
    }.map(emotions => Ok(Json.arr(emotions).apply(0)))
  }

  def updateEmotion(id: Long) = Action.async(parse.json) {
    request =>
      request.body.validate[Emotion].map {
        emotion =>
          Future {
            DB.withConnection { implicit conn =>
              SQL(
                """update Emotions set
                  |  userId={userId},
                  |  emotion={emotion},
                  |  reason={reason}, target={target},
                  |  location={location},
                  |  link={link}
                  |where id={id}""".stripMargin).
                on(emotion2NamedParams(emotion): _*).executeInsert()
            }
          }.flatMap(_ => Future.successful(Created(s"Emotion Created"))).andThen {
            case _ => logger.info(s"Emotion Updated: $emotion")
          }
      }.getOrElse(Future.successful(BadRequest("invalid json"))).andThen {
        case _ => logger.error(s"invalid json: ${request.body}")
      }
  }

  private def emotion2NamedParams(emotion: Emotion): List[NamedParameter] = {
    List("userId" -> emotion.userId,
      "emotion" -> emotion.emotion,
      "reason" -> emotion.reason,
      "target" -> emotion.target,
      "location" -> emotion.location,
      "link" -> emotion.link)
  }
}
