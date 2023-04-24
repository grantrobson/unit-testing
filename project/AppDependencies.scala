import sbt._

object AppDependencies {
  private val compile = Seq(
    "com.google.inject.extensions" % "guice-multibindings" % "4.2.2",
    "org.typelevel" %% "cats-core" % "2.9.0"
  )

  private val test = Seq(
    "org.scalatest" %% "scalatest" % "3.2.3",
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0",
    "org.jsoup" % "jsoup" % "1.10.3",
    "org.mockito" % "mockito-core" % "4.0.0",
    "org.mockito" %% "mockito-scala" % "1.17.5",
    "org.scalacheck" %% "scalacheck" % "1.15.2",
    "org.scalatestplus" %% "scalatestplus-scalacheck" % "3.1.0.0-RC2",
    "io.github.wolfendale"         %% "scalacheck-gen-regexp"  % "1.1.0"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
