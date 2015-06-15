package models.users

import securesocial.core.BasicProfile

/**
 * Created by yuri.zelikov on 6/14/2015.
 */

case class BasicUser(main: BasicProfile, identities: List[BasicProfile])
