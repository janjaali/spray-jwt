import scala.sys.process._

name := "spray-jwt"

organization := "com.github.janjaali"

version := "1.0.0"

licenses := Seq("MIT License" -> url("https://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/janjaali/spray-jwt"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/janjaali/spray-jwt"),
    "scm:git@github.com/janjaali/spray-jwt.git"
  )
)

developers := List(
  Developer(
    id = "ghashange",
    name = "ghashange",
    email = "",
    url = url("https://github.com/janjaali")
  )
)

scalaVersion := "2.12.3"

publishMavenStyle := true

publishArtifact in Test := false

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) {
    Some("snapshots" at nexus + "content/repositories/snapshots")
  } else {
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }
}

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

lazy val installGitHook = taskKey[Unit]("Installs git hooks")
installGitHook := {
  if (sys.props("os.name").contains("Windows")) {
    "cmd /c copy scripts\\pre-commit-hook.sh .git\\hooks\\pre-commit" !
  } else {
    "cp scripts/pre-commit-hook.sh .git/hooks/pre-commit" !
  }
}

(compile in Compile) := ((compile in Compile) dependsOn installGitHook).value
