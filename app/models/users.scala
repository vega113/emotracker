package models

import models.UserTableQueries.{profiles, oauth1s, oauth2s, passwords}
import org.joda.time.DateTime
import securesocial.core.providers.MailToken
import securesocial.core._

import com.github.tototoshi.slick.JdbcJodaSupport._
import org.joda.time.DateTime
import securesocial.core.providers.MailToken

import scala.slick.ast.ColumnOption.DBType
import scala.slick.driver.JdbcDriver.simple._

/**
 * Created by yuri.zelikov on 6/14/2015.
 */

case class BasicUser(main: BasicProfile, identities: List[BasicProfile])

class MailTokens(tag: Tag) extends Table[MailToken](tag, "mail_token") {
  def uuid = column[String]("uuid", O.PrimaryKey)
  def email = column[String]("email")
  def creationTime = column[DateTime]("creation_time")
  def expirationTime = column[DateTime]("expiration_time")
  def isSignUp = column[Boolean]("is_sign_up")

  def * = (uuid, email, creationTime, expirationTime, isSignUp) <> (MailToken.tupled, MailToken.unapply)
}

case class OAuth1(id: Option[Long] = None, token: String, secret: String) {
  def oAuth1Info: OAuth1Info = {
    OAuth1Info(token, secret)
  }
}

class OAuth1s(tag: Tag) extends Table[OAuth1](tag, "oauth1") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def token = column[String]("token")
  def secret = column[String]("secret")

  def * = (id.?, token, secret) <> (OAuth1.tupled, OAuth1.unapply)
}

case class OAuth2(id: Option[Long] = None, accessToken: String, tokenType: Option[String] = None,
                  expiresIn: Option[Int] = None, refreshToken: Option[String] = None) {
  def oAuth2Info: OAuth2Info = {
    OAuth2Info(accessToken, tokenType, expiresIn, refreshToken)
  }
}

class OAuth2s(tag: Tag) extends Table[OAuth2](tag, "oauth2") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def accessToken = column[String]("access_token")
  def tokenType = column[Option[String]]("token_type")
  def expiresIn = column[Option[Int]]("expires_in")
  def refreshToken = column[Option[String]]("refresh_token")

  def * = (id.?, accessToken, tokenType, expiresIn, refreshToken) <> (OAuth2.tupled, OAuth2.unapply)
}

case class Password(id: Option[Long] = None, hasher: String, password: String, salt: Option[String] = None) {
  def passwordInfo: PasswordInfo = {
    PasswordInfo(hasher, password, salt)
  }
}

class Passwords(tag: Tag) extends Table[Password](tag, "password") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def hasher = column[String]("hasher")
  def password = column[String]("password")
  def salt = column[Option[String]]("salt")

  def * = (id.?, hasher, password, salt) <> (Password.tupled, Password.unapply)
}

case class Profile(id: Option[Long] = None,
                   providerId: String,
                   userId: String,
                   firstName: Option[String] = None,
                   lastName: Option[String] = None,
                   fullName: Option[String] = None,
                   email: Option[String] = None,
                   avatarUrl: Option[String] = None,
                   authMethod: String,
                   oAuth1Id: Option[Long] = None,
                   oAuth2Id: Option[Long] = None,
                   passwordId: Option[Long] = None) extends UserProfile {
  def basicProfile(implicit session: Session): BasicProfile = {
    BasicProfile(
      providerId,
      userId,
      firstName,
      lastName,
      fullName,
      email,
      avatarUrl,
      authMethod match {
        case "oauth1" => AuthenticationMethod.OAuth1
        case "oauth2" => AuthenticationMethod.OAuth2
        case "openId" => AuthenticationMethod.OpenId
        case "userPassword" => AuthenticationMethod.UserPassword
      },
      oauth1s.filter(_.id === oAuth1Id).firstOption.map(o1 => o1.oAuth1Info),
      oauth2s.filter(_.id === oAuth2Id).firstOption.map(o2 => o2.oAuth2Info),
      passwords.filter(_.id === passwordId).firstOption.map(p => p.passwordInfo)
    )
  }
}

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

case class UserAuthenticator(id: String, userId: String, expirationDate: DateTime, lastUsed: DateTime, creationDate: DateTime)

class UserAuthenticators(tag: Tag) extends Table[UserAuthenticator](tag, "authenticator") {
  def id = column[String]("id", DBType("varchar(2000)"), O.PrimaryKey)
  def userId = column[String]("user_id")
  def expirationDate = column[DateTime]("expiration_date")
  def lastUsed = column[DateTime]("last_used")
  def creationDate = column[DateTime]("creation_date")

  def * = (id, userId, expirationDate, lastUsed, creationDate) <> (UserAuthenticator.tupled, UserAuthenticator.unapply)
}

case class User(id: String, mainId: Long) {
  def basicUser(implicit session: Session): BasicUser = {
    val main = profiles.filter(_.id === mainId).first
    val identities = profiles.filter(p => p.userId === id && p.id =!= mainId).list

    BasicUser(main.basicProfile, identities.map(i => i.basicProfile))
  }
}

class Users(tag: Tag) extends Table[User](tag, "user") {
  def id = column[String]("id", O.PrimaryKey)
  def mainId = column[Long]("main_id")

  def * = (id, mainId) <> (User.tupled, User.unapply)
}