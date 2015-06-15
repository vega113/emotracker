package models.users

import securesocial.core.PasswordInfo

/**
 * Created by yuri.zelikov on 6/15/2015.
 */
case class Password(id: Option[Long] = None, hasher: String, password: String, salt: Option[String] = None) {
  def passwordInfo: PasswordInfo = {
    PasswordInfo(hasher, password, salt)
  }
}
