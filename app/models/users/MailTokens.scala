package models.users

import com.github.tototoshi.slick.JdbcJodaSupport._
import org.joda.time.DateTime
import securesocial.core.providers.MailToken

import scala.slick.driver.JdbcDriver.simple._

/**
 * Created by yuri.zelikov on 6/15/2015.
 */
class MailTokens(tag: Tag) extends Table[MailToken](tag, "mail_token") {
  def uuid = column[String]("uuid", O.PrimaryKey)
  def email = column[String]("email")
  def creationTime = column[DateTime]("creation_time")
  def expirationTime = column[DateTime]("expiration_time")
  def isSignUp = column[Boolean]("is_sign_up")

  def * = (uuid, email, creationTime, expirationTime, isSignUp) <> (MailToken.tupled, MailToken.unapply)
}
