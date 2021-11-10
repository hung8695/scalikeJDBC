package repositories
import models.User
import scalikejdbc._
import scalikejdbc.jodatime.JodaBinders

trait UserRepo {
  def findByEmail(email: String): Option[User]

  def findByUserName(userName: String): Option[User]

}

class UserRepoImpl extends UserRepo {
  implicit val time: TypeBinder[org.joda.time.DateTime] = JodaBinders.jodaDateTime
  def toMap(rs: WrappedResultSet): User = {
    new User(
      id = rs.get("id"),
      userName = rs.get("user_name"),
      email = rs.get("email"),
      password = rs.get("password"),
      firstName = rs.get("first_name"),
      lastName = rs.get("last_name"),
      age = rs.get("age"),
      role = rs.get("role"),
      createdAt = rs.get("created_at"),
      modifiedAt = rs.get("modified_at")
    )
  }

  override def findByEmail(email: String): Option[User] = DB readOnly { implicit session =>
    val emailTemp = email.toLowerCase()
    sql"SELECT * FROM users WHERE LOWER(email) = ${emailTemp}"
      .map { rs => toMap(rs) }
      .single().apply()
  }

  override def findByUserName(userName: String): Option[User] = DB readOnly { implicit session =>
    val userNameTemp = userName.toLowerCase()
    sql"SELECT * FROM users WHERE LOWER(user_name) = ${userNameTemp}"
      .map { rs => toMap(rs) }
      .single().apply()
  }

}
