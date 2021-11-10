package controllers

import javax.inject.Inject
import play.api.mvc.Results._
import play.api.mvc._
import property.PropertyConstants

import scala.concurrent.{ ExecutionContext, Future }

class AuthenticatedUserAction @Inject() (parser: BodyParsers.Default)(implicit ec: ExecutionContext)

  extends ActionBuilderImpl(parser) {

  private val logger = play.api.Logger(this.getClass)

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    val username = request.session.get(PropertyConstants.SESSION_USERNAME_KEY)
    username match {
      case None => {
        Future.successful(Forbidden("You’re not logged in."))
      }
      case Some(u) => {
        val res: Future[Result] = block(request)
        res
      }
    }
  }
}
