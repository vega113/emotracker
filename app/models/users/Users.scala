package models.users

import scala.slick.driver.JdbcDriver.simple._
/**
 * Created by yuri.zelikov on 6/15/2015.
 */
class Users(tag: Tag) extends Table[User](tag, "user") {
  def id = column[String]("id", O.PrimaryKey)
  def mainId = column[Long]("main_id")

  def * = (id, mainId) <> (User.tupled, User.unapply)
}
