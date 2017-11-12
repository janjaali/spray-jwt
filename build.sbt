import scala.sys.process._

name := "spray-jwt"

organization := "net.habashi"

version := "1.0.0"

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

lazy val installGitHook = taskKey[Unit]("Installs git hooks")
installGitHook := {
  if (sys.props("os.name").contains("Windows")) {
    "cmd /c copy scripts\\pre-commit-hook.sh .git\\hooks\\pre-commit" !
  } else {
    "cp scripts/pre-commit-hook.sh .git/hooks/pre-commit" !
  }
}

(compile in Compile) := ((compile in Compile) dependsOn installGitHook).value
