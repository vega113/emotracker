package models.users

import scala.slick.driver.JdbcDriver.simple._

/**
 * Created by yuri.zelikov on 6/15/2015.
 */
class Passwords(tag: Tag) extends Table[Password](tag, "password") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def hasher = column[String]("hasher")
  def password = column[String]("password")
  def salt = column[Option[String]]("salt")

  def * = (id.?, hasher, password, salt) <> (Password.tupled, Password.unapply)
}
