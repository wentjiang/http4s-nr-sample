package models

import cats.implicits.toFunctorOps
import doobie._
import doobie.implicits._

object DatabaseInitializer {
  def initialize: ConnectionIO[Unit] =
    sql"""
      CREATE TABLE IF NOT EXISTS users (
        id SERIAL,
        name VARCHAR NOT NULL
      )
    """.update.run.void
}