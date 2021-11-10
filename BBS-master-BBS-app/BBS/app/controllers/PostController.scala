package controllers

import javax.inject._
import play.api.mvc._
import services.{ PostServiceImpl, UserServiceImpl }
import models.{ Post, PostForm, User }
import org.joda.time.DateTime
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, AnyContent, ControllerComponents, Request }

import scala.util.{ Failure, Success, Try }

@Singleton
class PostController @Inject() (cc: ControllerComponents, authen: AuthenticatedUserAction,
    postService: PostServiceImpl, usersService: UserServiceImpl) extends AbstractController(cc) with I18nSupport {

  def findAll: Action[AnyContent] = Action { implicit request =>
    val posts = postService.findAll
    if (posts.isSuccess) Ok(views.html.post.posts(posts.get))
    else BadRequest(views.html.errMessage("Something Wrong!"))
  }

  def findById(id: Long): Action[AnyContent] = Action {
    val post = postService.findById(id)
    post match {
      case Some(post) => Ok(views.html.post.postDetail(post))
      case None       => BadRequest(views.html.errMessage("Not found!"))
    }
  }

  def createPost() = authen { implicit request =>

    PostForm.form.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.errMessage("Something Wrong!"))
      },
      postData => {
        val username = request.session.get("username")
        val currentUser: Option[User] = usersService.findByUserName(username.get);
        currentUser match {
          case None => BadRequest(views.html.errMessage("User not exist in System!"))
          case Some(user) => {
            val result = for {
              postForm <- Try(postData)
              post = new Post(0, postForm.category, postForm.title, postForm.content, 0, true, "", false, new DateTime(), new DateTime())
              id <- postService.createPost(post)
            } yield id

            result match {
              case Success(id) =>
                Redirect(routes.PostController.findAll).flashing("info" -> "Create Post success!")
              case Failure(e) => e match {
                case _ => BadRequest(views.html.errMessage("Something Wrong!"))
              }
            }
          }
        }
      }
    )
  }

  def createForm() = authen { implicit request =>
    Ok(views.html.post.createPost(PostForm.form))
  }

}