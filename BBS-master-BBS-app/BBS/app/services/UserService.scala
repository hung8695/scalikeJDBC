package services

import javax.inject.{ Inject, Singleton }
import models.User
import repositories.UserRepoImpl

trait UserService {
  def findByEmail(email: String): Option[User]

  def findByUserName(userName: String): Option[User]
}

@Singleton
class UserServiceImpl @Inject() (userDAO: UserRepoImpl) extends UserService {
  override def findByEmail(email: String): Option[User] = {
    userDAO.findByEmail(email)
  }

  override def findByUserName(userName: String): Option[User] = {
    userDAO.findByUserName(userName)
  }
}
