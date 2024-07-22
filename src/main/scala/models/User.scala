package models

import doobie._
import doobie.implicits._

case class User(id: Long, name: String)


object UserDAO {
  def create(name: String): ConnectionIO[User] =
    sql"INSERT INTO users (name) VALUES ($name)".update
      .withUniqueGeneratedKeys[User]("id", "name")

  def find(id: Long): ConnectionIO[Option[User]] =
    sql"SELECT id, name FROM users WHERE id = $id".query[User].option

  def list: ConnectionIO[List[User]] =
    sql"SELECT id, name FROM users".query[User].to[List]
}
