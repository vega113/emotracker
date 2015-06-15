package models.users

import securesocial.core.OAuth2Info

/**
 * Created by yuri.zelikov on 6/15/2015.
 */
case class OAuth2(id: Option[Long] = None, accessToken: String, tokenType: Option[String] = None,
                  expiresIn: Option[Int] = None, refreshToken: Option[String] = None) {
  def oAuth2Info: OAuth2Info = {
    OAuth2Info(accessToken, tokenType, expiresIn, refreshToken)
  }
}
