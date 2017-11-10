name := "spray-jwt"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.3"

val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)

val dependencies = Seq(
  "io.spray" %% "spray-json" % "1.3.3",
  "org.bouncycastle" % "bcpkix-jdk15on" % "1.58"
)

libraryDependencies ++= dependencies
libraryDependencies ++= testDependencies

lazy val scalastyleTest = taskKey[Unit]("scalastyleTest")
scalastyleTest := (scalastyle in Test).toTask("").value

(scalastyle in Compile) := ((scalastyle in Compile) dependsOn scalastyleTest).toTask("").value
