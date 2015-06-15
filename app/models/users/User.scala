package models.users

import models.UserTableQueries.profiles

import scala.slick.driver.JdbcDriver.simple._

/**
 * Created by yuri.zelikov on 6/15/2015.
 */
case class User(id: String, mainId: Long) {
  def basicUser(implicit session: Session): BasicUser = {
    val main = profiles.filter(_.id === mainId).first
    val identities = profiles.filter(p => p.userId === id && p.id =!= mainId).list

    BasicUser(main.basicProfile, identities.map(i => i.basicProfile))
  }
}
