package controllers

import javax.inject.{ Inject, Singleton }
import models.{ User, UserLoginForm }
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, AnyContent, ControllerComponents, Request }
import property.PropertyConstants
import services._

@Singleton
class AuthenticatedUserController @Inject() (cc: ControllerComponents, authenticatedUserAction: AuthenticatedUserAction, usersService: UserServiceImpl) extends AbstractController(cc) with I18nSupport {

  def loginForm() = Action { implicit request =>
    Ok(views.html.login(UserLoginForm.form))
  }

  def login = Action { implicit request =>
    val errorFunction = { formWithErrors: Form[UserLoginForm] =>
      BadRequest(views.html.login(formWithErrors))
    }
    val successFunction = { userLoginForm: UserLoginForm =>
      val user: Option[User] = usersService.findByEmail(userLoginForm.email)
      user match {
        case Some(user) => {
          if (user.password == userLoginForm.password)
            Redirect(routes.PostController.findAll)
              .flashing("info" -> "You are logged in.")
              .withSession(PropertyConstants.SESSION_USERNAME_KEY -> user.userName)
          else
            Redirect(routes.AuthenticatedUserController.loginForm)
              .flashing("error" -> PropertyConstants.MSG_ERROR_LOGIN)
        }
        case None => Redirect(routes.AuthenticatedUserController.loginForm)
          .flashing("error" -> PropertyConstants.MSG_ERROR_LOGIN)
      }
    }
    val formValidationResult: Form[UserLoginForm] = UserLoginForm.form.bindFromRequest
    formValidationResult.fold(
      errorFunction,
      successFunction
    )
  }

  def logout = authenticatedUserAction { implicit request: Request[AnyContent] =>
    Redirect(routes.AuthenticatedUserController.loginForm)
      .flashing("info" -> "You are logged out.")
      .withNewSession
  }
}
