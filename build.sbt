ThisBuild / scalaVersion := "3.3.6"

lazy val root = (project in file("."))
  .enablePlugins(play.sbt.PlayScala)
  .settings(
    name := "unit-testing",
    libraryDependencies ++= AppDependencies()
  )

Compile / scalaSource := baseDirectory.value / "src/main/scala"
Test / scalaSource := baseDirectory.value / "src/test/scala"     