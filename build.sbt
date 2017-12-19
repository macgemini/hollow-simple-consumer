name := """hollow-simple-consumer"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

libraryDependencies += "org.clapper" %% "classutil" % "1.1.2"
libraryDependencies += "io.spray" % "spray-json_2.11" % "1.3.2"
libraryDependencies += "com.netflix.hollow" % "hollow" % "2.6.8"
libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-nop" % "1.6.4"
)
libraryDependencies += "com.netflix.hollow" % "hollow-ui-tools" % "2.6.8"
libraryDependencies += "com.netflix.hollow" % "hollow-explorer-ui" % "2.6.8"
libraryDependencies += "com.netflix.hollow" % "hollow-diff-ui" % "2.6.8"
libraryDependencies += "org.eclipse.jetty" % "jetty-server" % "9.4.0.M0"


