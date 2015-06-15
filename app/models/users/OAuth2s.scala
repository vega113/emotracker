package models.users

import scala.slick.driver.JdbcDriver.simple._

/**
 * Created by yuri.zelikov on 6/15/2015.
 */
class OAuth2s(tag: Tag) extends Table[OAuth2](tag, "oauth2") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def accessToken = column[String]("access_token")
  def tokenType = column[Option[String]]("token_type")
  def expiresIn = column[Option[Int]]("expires_in")
  def refreshToken = column[Option[String]]("refresh_token")

  def * = (id.?, accessToken, tokenType, expiresIn, refreshToken) <> (OAuth2.tupled, OAuth2.unapply)
}
