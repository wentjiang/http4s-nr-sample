import org.apache.logging.log4j.core.config.composite.MergeStrategy
import sun.security.tools.PathList
import sbt.Keys.{fork, scalacOptions}
name := "http4s-nr-sample"
version := "1.0"

scalaVersion := "2.13.13"

val http4sVersion = "0.23.16"
fork := true

Compile / mainClass := Some("Main")

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl"              % http4sVersion,
  "org.http4s" %% "http4s-blaze-server"     % http4sVersion,
  "org.http4s" %% "http4s-circe"            % http4sVersion,
  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-parser"  % "0.14.1",
  "org.tpolecat" %% "doobie-core"     % "1.0.0-RC1",
  "org.tpolecat" %% "doobie-hikari"   % "1.0.0-RC1",
  "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC1",

)

scalacOptions ++= Seq(
  "-Wdead-code",
  "-Werror",
  "-Wextra-implicit",
  "-Wnumeric-widen",
  "-Wunused",
  "-Wvalue-discard",
  "-Xlint",
  "-Xlint:implicit-recursion",
  "-Xlint:-byname-implicit", // As of Scala 2.13.4 `deriveEncoder[T]` breaks
  "-deprecation",
  "-feature",
  "-language:higherKinds",
  "-Ywarn-macros:after"
)

Compile / console / scalacOptions --= Seq(
  "-Werror",
  "-Wunused",
  "-Xlint"
)

assembly / assemblyOutputPath := new File("target/app.jar")
