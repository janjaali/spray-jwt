scalaVersion := "2.13.4"

ThisBuild / versionScheme := Some("early-semver")

lazy val sprayJwt = (project in file("spray-jwt"))
  .settings(
    name := "spray-jwt",
    organization := "com.github.janjaali",
    version := "1.0.0",
    licenses := Seq("MIT License" -> url("https://opensource.org/licenses/MIT")),
    homepage := Some(url("https://github.com/janjaali/spray-jwt")),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/janjaali/spray-jwt"),
        "scm:git@github.com/janjaali/spray-jwt.git"
      )
    ),
    developers := List(
      Developer(
        id = "ghashange",
        name = "ghashange",
        email = "",
        url = url("https://github.com/janjaali")
      )
    ),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value) {
        Some("snapshots" at nexus + "content/repositories/snapshots")
      } else {
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
      }
    },
    libraryDependencies ++= Seq(
      // JSON
      "io.spray" %% "spray-json" % "1.3.3",

      // Encryption
      "org.bouncycastle" % "bcpkix-jdk15on" % "1.58",

      // Test
      "org.scalatest" %% "scalatest" % "3.2.6" % Test,

      // Property based tests
      "org.scalacheck" %% "scalacheck" % "1.15.3" % Test,
      "org.scalatestplus" %% "scalacheck-1-15" % "3.2.6.0" % Test
    )
  )

lazy val sprayJwtAkkaHttpTest = (project in file("spray-jwt-akka-http-test"))
  .dependsOn(sprayJwt)
  .settings(
    name := "spray-jwt-akka-http-test",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % "10.0.11",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.0.11" % Test,
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.11",

      "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    )
  )
