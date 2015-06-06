package models

import play.api.libs.json.Format

case class Emotion(id: Option[Long],
                   userId: String,
                   emotion: String,
                   reason: Option[String],
                   target: Option[String],
                   location: Option[String],
                   link: Option[String])

object JsonFormats {

  import play.api.libs.json.Json

  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val emotionFormat: Format[Emotion] = Json.format[Emotion]
}