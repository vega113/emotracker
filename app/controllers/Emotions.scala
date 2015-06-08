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
              SQL("insert into Emotions (userId, emotion, reason, target, location, link) values ({userId}, {emotion}, {reason}, '', '', '')").
                on("userId" -> emotion.userId, "emotion" -> emotion.emotion, "reason" -> emotion.reason).executeInsert()
            }
          }.flatMap(_ => Future.successful(Created(s"User Created")))

      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }


  lazy val p: ResultSetParser[List[(Long, String, String, String, String, String, String)]] = {
    (long("id") ~ str("userId") ~ str("emotion") ~ str("reason") ~ str("target")  ~ str("location") ~ str("link")).map(flatten).*
  }

  def findEmotions = Action.async {
    Future {
      DB.withConnection { implicit conn =>
          SQL("select * from Emotions").executeQuery().parse(p).
            map(x => Emotion(Option(x._1), x._2, x._3, Option(x._4), Option(x._5), Option(x._6), Option(x._7)))
      }
    }.map(emotions => Ok(Json.arr(emotions).apply(0)))
  }

  def updateEmotion(id: Long) = Action.async {
    Future {
      DB.withConnection { implicit conn =>
        SQL("select * from Emotions").executeQuery().parse(p).
          map(x => Emotion(Option(x._1), x._2, x._3, Option(x._4), Option(x._5), Option(x._6), Option(x._7)))
      }
    }.map(emotions => Ok(Json.arr(emotions).apply(0)))
  }
}
