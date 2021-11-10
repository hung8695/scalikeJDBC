package models

import org.joda.time.DateTime
import play.api.data._
import play.api.data.Forms._

case class Post(
    id: Long,
    category: String,
    title: String,
    content: String,
    userId: Long,
    published: Boolean,
    email: String,
    isDeleted: Boolean,
    createdAt: DateTime,
    modifiedAt: DateTime) {
}

case class PostForm(
    category: String,
    title: String,
    content: String)

object PostForm {
  val form: Form[PostForm] = Form(
    mapping(
      "category" -> nonEmptyText.verifying("too many chars", s => { if (s.length < 100) true else false }),
      "title" -> nonEmptyText.verifying("too many chars", s => { if (s.length < 200) true else false }),
      "content" -> nonEmptyText.verifying("too many chars", s => { if (s.length < 2000) true else false })
    )(PostForm.apply)(PostForm.unapply)
  )
}

