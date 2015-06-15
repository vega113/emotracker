package models.users

import scala.slick.driver.JdbcDriver.simple._

/**
 * Created by yuri.zelikov on 6/15/2015.
 */
class Profiles(tag: Tag) extends Table[Profile](tag, "profile") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def providerId = column[String]("provider_id")
  def userId = column[String]("user_id")
  def firstName = column[Option[String]]("first_name")
  def lastName = column[Option[String]]("last_name")
  def fullName = column[Option[String]]("full_name")
  def email = column[Option[String]]("email")
  def avatarUrl = column[Option[String]]("avatar_url")
  def authMethod = column[String]("auth_method")
  def oAuth1Id = column[Option[Long]]("oauth1_id")
  def oAuth2Id = column[Option[Long]]("oauth2_id")
  def passwordId = column[Option[Long]]("password_id")

  def * = (
    id.?,
    providerId,
    userId,
    firstName,
    lastName,
    fullName,
    email,
    avatarUrl,
    authMethod,
    oAuth1Id,
    oAuth2Id,
    passwordId
    ) <> (Profile.tupled, Profile.unapply)

  def idk = index("profile_idx", (providerId, userId))
}
