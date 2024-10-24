name := """json2apex"""
organization := "com.johnforeland"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.10"

libraryDependencies += "com.typesafe.play" % "sbt-plugin" % "2.8.19"

// libraryDependencies += jdbc
// libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
// libraryDependencies ++= Seq(
//   "com.google.inject"            % "guice"                % "5.1.0",
//   "com.google.inject.extensions" % "guice-assistedinject" % "5.1.0",
//   "org.postgresql" % "postgresql" % "42.6.0"
// )

// Adds additional packages into Twirl
// TwirlKeys.templateImports += "com.heroku.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.heroku.binders._"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

initialize := {
  val _ = initialize.value
  val javaVersion = sys.props("java.specification.version")
  if (javaVersion != "1.8")
    sys.error("Java 1.8 is required for this project. Found " + javaVersion + " instead")
}
