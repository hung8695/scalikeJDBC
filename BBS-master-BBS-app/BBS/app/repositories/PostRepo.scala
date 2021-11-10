package repositories

import models.Post
import org.joda.time.DateTime
import scalikejdbc._
import scalikejdbc.jodatime.JodaBinders
import skinny.orm._

import scala.util.Try

trait PostRepo {
  def findById(id: Long): Option[Post]

  def findAll(): Try[List[Post]]
}

class PostRepoImpl extends PostRepo {
  implicit val time: TypeBinder[org.joda.time.DateTime] = JodaBinders.jodaDateTime

  def mappingData(rs: WrappedResultSet): Post = {
    new Post(
      id = rs.get("id"),
      category = rs.get("category"),
      title = rs.get("title"),
      content = rs.get("content"),
      userId = rs.get("user_id"),
      published = rs.get("published"),
      email = rs.get("email"),
      isDeleted = rs.get("is_deleted"),
      createdAt = rs.get("created_at"),
      modifiedAt = rs.get("modified_at")
    )
  }

  def findById(id: Long): Option[Post] = DB readOnly { implicit session =>
    sql"""SELECT
          p.id,
          p.category,
          p.title,
          p.content,
          p.user_id,
          p.published,
          p.is_deleted,
          p.created_at,
          p.modified_at,
          u.email
         FROM post AS p
         LEFT JOIN users AS u
          ON p.user_id = u.id
         WHERE p.id = ${id}"""
      .map { rs => mappingData(rs) }
      .single().apply()
  }

  override def findAll(): Try[List[Post]] = DB readOnly { implicit session =>
    Try {
      SQL(
        """SELECT
          p.id,
          p.category,
          p.title,
          p.content,
          p.user_id,
          p.published,
          p.is_deleted,
          p.created_at,
          p.modified_at,
          null AS email
         FROM post AS p
         WHERE p.is_deleted = 0 AND p.published = 1""")
        .map { rs => mappingData(rs) }
        .list().apply()
    }
  }
}

object PostRepo extends SkinnyCRUDMapper[Post] {

  override lazy val defaultAlias = createAlias("p")
  override lazy val tableName = "Post";

  override def extract(rs: WrappedResultSet, rn: ResultName[Post]): Post = new Post(
    id = rs.get(rn.id),
    category = rs.get(rn.category),
    title = rs.get(rn.title),
    content = rs.get(rn.content),
    userId = rs.get(rn.userId),
    email = "",
    published = rs.get(rn.published),
    isDeleted = rs.get(rn.isDeleted),
    createdAt = rs.get(rn.createdAt),
    modifiedAt = rs.get(rn.modifiedAt)
  )
}
