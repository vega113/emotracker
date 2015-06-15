package models.users

import models.UserTableQueries.{oauth1s, oauth2s, passwords}
import securesocial.core.{AuthenticationMethod, BasicProfile, UserProfile}
import scala.slick.driver.JdbcDriver.simple._

/**
 * Created by yuri.zelikov on 6/15/2015.
 */
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
