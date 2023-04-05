name := """numerator"""
organization := "com.dojo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

val AkkaVersion = "2.7.0"
scalaVersion := "2.13.10"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test
libraryDependencies += "net.codingwell" %% "scala-guice" % "5.1.0"
libraryDependencies += "com.typesafe.akka" %% "akka-persistence-typed" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-persistence-testkit" % AkkaVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-persistence-cassandra" % "1.1.0"
libraryDependencies += "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.dojo.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.dojo.binders._"
