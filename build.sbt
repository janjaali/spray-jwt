scalaVersion := "2.12.3"

lazy val sprayJwt = (project in file("spray-jwt"))
  .settings(
    name := "spray-jwt",
    organization := "com.github.janjaali",
    version := "1.0.0-SNAPSHOT",
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
      "io.spray" %% "spray-json" % "1.3.3",
      "org.bouncycastle" % "bcpkix-jdk15on" % "1.58",
      "org.scalatest" %% "scalatest" % "3.0.4" % Test
    )
  )
