package models.users

import securesocial.core.OAuth1Info

/**
 * Created by yuri.zelikov on 6/15/2015.
 */
case class OAuth1(id: Option[Long] = None, token: String, secret: String) {
  def oAuth1Info: OAuth1Info = {
    OAuth1Info(token, secret)
  }
}
