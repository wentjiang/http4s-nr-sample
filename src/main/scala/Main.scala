import cats.effect.{ExitCode, IO, IOApp}
import doobie.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import models.{DatabaseInitializer, UserService}
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._

import scala.concurrent.ExecutionContext.global

object Main extends IOApp{

  def run(args: List[String]): IO[ExitCode] = {
    val transactorResource = DatabaseConfig.transactor


    transactorResource.use {
      xa => {
        val userService = new UserService(xa)

        val httpApp = HttpRoutes.of[IO] {
          case _@GET -> Root / "helloWorld" =>
            Ok("Hello world!")

          case GET -> Root / "hello" / name =>
            Ok(s"Hello, $name.")

          case GET -> Root / "user" / LongVar(id) =>
            for {
              user <- userService.getUser(id)
              resp <- user match {
                case Some(u) => Ok(u.asJson)
                case None => NotFound(s"User with id $id not found")
              }
            } yield resp

          case req @ POST -> Root / "user" =>
            for {
              name <- req.as[String]
              user <- userService.createUser(name)
              resp <- Ok(user.asJson)
            } yield resp
        }.orNotFound


        for {
          // init db
          _ <- DatabaseInitializer.initialize.transact(xa)

          exitCode <-   BlazeServerBuilder[IO]
            .withExecutionContext(global)
            .bindHttp(8083, "0.0.0.0")
            .withHttpApp(httpApp)
            .serve
            .compile
            .drain
            .as(ExitCode.Success)
        } yield exitCode
      }
    }
  }
}
