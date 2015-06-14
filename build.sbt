name := "emotracker"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.4"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "com.google.inject" % "guice" % "3.0",
  "javax.inject" % "javax.inject" % "1",
  "org.webjars" % "bootstrap" % "3.3.1",
  "org.webjars" % "angularjs" % "1.3.8",
  "org.webjars" % "angular-ui-bootstrap" % "0.12.0",
  "org.mockito" % "mockito-core" % "1.10.17" % "test")

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.27"

libraryDependencies ++= Seq(
  jdbc,
  anorm
)

libraryDependencies += "ws.securesocial" %% "securesocial" % "master-SNAPSHOT"

libraryDependencies += "com.typesafe.play" %% "play-slick" % "0.8.0"

libraryDependencies += "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0"
