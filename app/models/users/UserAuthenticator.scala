package models.users

import org.joda.time.DateTime

/**
 * Created by yuri.zelikov on 6/15/2015.
 */
case class UserAuthenticator(id: String, userId: String, expirationDate: DateTime, lastUsed: DateTime, creationDate: DateTime)
