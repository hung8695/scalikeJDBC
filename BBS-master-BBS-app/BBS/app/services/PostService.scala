package services

import property.PropertyConstants
import javax.inject._
import models.Post
import org.joda.time.DateTime
import repositories.{ PostRepo, PostRepoImpl }

import scala.util.Try

trait PostService {

  def findById(id: Long): Option[Post]

  def findAll: Try[List[Post]]

  def createPost(post: Post): Try[Long]

}

@Singleton
class PostServiceImpl @Inject() (postRepo: PostRepoImpl) extends PostService {

  override def findAll: Try[List[Post]] = postRepo.findAll()

  override def findById(id: Long): Option[Post] = {
    postRepo.findById(id)
  }

  override def createPost(post: Post): Try[Long] = {
    Try {
      PostRepo.createWithAttributes('category -> post.category, 'title -> post.title, 'content -> post.content, 'user_id -> post.userId, 'published -> PropertyConstants.POST_DEFAULT_PUBLISH, 'is_deleted -> 0, 'created_at -> post.createdAt, 'modified_at -> post.modifiedAt)
    }
  }

}
