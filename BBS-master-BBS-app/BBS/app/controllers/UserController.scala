package controllers

import javax.inject.{ Inject, Singleton }
import play.api.mvc.{ AbstractController, ControllerComponents }
import services._

@Singleton
class UserController @Inject() (
    cc: ControllerComponents,
    userService: UserService) extends AbstractController(cc) {

}
