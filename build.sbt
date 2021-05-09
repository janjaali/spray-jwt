ThisBuild / scalaVersion := "3.0.0-RC3"

ThisBuild / versionScheme := Some("early-semver")

lazy val sprayJwt = (project in file("spray-jwt"))
  .settings(
    name := "spray-jwt",
    organization := "com.github.janjaali",
    version := "1.0.0",
    
    licenses := Seq(
      "MIT License" -> url("https://opensource.org/licenses/MIT")
    ),
    homepage := Some(url("https://github.com/janjaali/spray-jwt")),
    scmInfo := Some(
      ScmInfo(
        browseUrl = url("https://github.com/janjaali/spray-jwt"),
        connection = "scm:git@github.com/janjaali/spray-jwt.git"
      )
    ),
    developers := List(
      Developer(
        id = "janjaali",
        name = "janjaali",
        email = "",
        url = url("https://github.com/janjaali")
      )
    ),
    
    publishMavenStyle := true,
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
      ("io.spray" %% "spray-json" % "1.3.6").cross(CrossVersion.for3Use2_13),
      // Encryption
      "org.bouncycastle" % "bcpkix-jdk15on" % "1.58",
      // Test
      ("org.scalatest" %% "scalatest" % "3.2.7" % Test).cross(CrossVersion.for3Use2_13),
      // Property based tests
      ("org.scalacheck" %% "scalacheck" % "1.15.3" % Test).cross(CrossVersion.for3Use2_13),
      ("org.scalatestplus" %% "scalacheck-1-15" % "3.2.6.0" % Test).cross(CrossVersion.for3Use2_13)
    )
  )

lazy val sprayJsonSupport = (project in file("spray-json-support"))
  .dependsOn(sprayJwt % "test->test;compile->compile")
  .settings {
    libraryDependencies ++= Seq(
      // Supported JSON library
      ("io.spray" %% "spray-json" % "1.3.6").cross(CrossVersion.for3Use2_13)
    )
  }
