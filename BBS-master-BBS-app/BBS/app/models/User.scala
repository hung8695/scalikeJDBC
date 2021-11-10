package models

import org.joda.time.DateTime
import play.api.data.Form
import play.api.data.Forms._

case class User(
    id: Long,
    userName: String,
    email: String,
    password: String,
    firstName: String,
    lastName: String,
    age: Int,
    role: String,
    createdAt: DateTime,
    modifiedAt: DateTime) {
}

case class UserLoginForm(
    email: String,
    password: String)

object UserLoginForm {
  val form: Form[UserLoginForm] = Form(
    mapping(
      "email" -> email.verifying("Email too many chars", s => {
        if (s.length < 41) true else false
      }),
      "password" -> nonEmptyText.verifying("Password too few chars", s => {
        if (s.length > 7) true else false
      })
    )(UserLoginForm.apply)(UserLoginForm.unapply)
  )
}
