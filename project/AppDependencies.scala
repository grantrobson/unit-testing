import sbt._

object AppDependencies {
  private val compile = Seq(
    "com.google.inject.extensions" % "guice-multibindings" % "4.2.2",
    "org.typelevel" %% "cats-core" % "2.9.0"
  )

  private val test = Seq(
    "org.scalatest"         %% "scalatest"               % "3.2.16",
//  "org.scalatestplus.play" %% "scalatestplus-play" % "x.x.x" % "test",
// "org.scalatestplus" %% "mockito-5-18" % "3.2.19.0" % "test",
//    "org.scalatestplus" %% "mockito-4-11" % "3.2.19.0" % "test",
  //    "org.scalatest" %% "scalatest" % "3.2.19",

      "org.scalatestplus" %% "mockito-5-10" % "3.2.18.0",

//    "org.scalatestplus"     %% "scalatestplus-scalacheck"   % "3.2.19.0",      
//      "org.scalatest" %% "scalatest" % "3.2.18" % Test

  "org.jsoup" % "jsoup" % "1.10.3",
//    "org.scalatestplus" %% "mockito-5-18" % "3.2.19.0" % "test",
    "org.scalatest" %% "scalatest" % "3.2.18",
    "io.github.wolfendale"         %% "scalacheck-gen-regexp"  % "1.1.0",
    "org.scala-lang.modules" %% "scala-parser-combinators" % "2.4.0"

  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
