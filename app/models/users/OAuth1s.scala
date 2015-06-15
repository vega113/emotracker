package models.users

import securesocial.core.OAuth1Info

import scala.slick.driver.JdbcDriver.simple._

/**
 * Created by yuri.zelikov on 6/15/2015.
 */
class OAuth1s(tag: Tag) extends Table[OAuth1](tag, "oauth1") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def token = column[String]("token")
  def secret = column[String]("secret")

  def * = (id.?, token, secret) <> (OAuth1.tupled, OAuth1.unapply)
}
