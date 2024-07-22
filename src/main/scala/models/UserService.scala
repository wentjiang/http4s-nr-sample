package models

import doobie.util.transactor.Transactor
import doobie.implicits._
import cats.effect.IO

class UserService(xa: Transactor[IO]) {
  def createUser(name: String): IO[User] =
    UserDAO.create(name).transact(xa)

  def getUser(id: Long): IO[Option[User]] =
    UserDAO.find(id).transact(xa)

  def listUsers: IO[List[User]] =
    UserDAO.list.transact(xa)
}