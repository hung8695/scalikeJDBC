import javax.inject.Singleton
import play.api.http.HttpErrorHandler
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent._

@Singleton
class ErrorHandler extends HttpErrorHandler {

  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful(
      statusCode match {
        case 400 => Ok(views.html.errMessage(" Oops!!!! BAD REQUEST"))
        case 401 => Ok(views.html.errMessage(" Oops!!!! UNAUTHORIZED"))
        case 404 => Ok(views.html.errMessage(" Oops!!!! Page not Found"))
        case 500 => Ok(views.html.errMessage(" Oops!!!! Internal Server Error"))
      }
    )
  }

  def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    Future.successful(
      InternalServerError("A server error occurred: " + exception.getMessage)
    )
  }
}
