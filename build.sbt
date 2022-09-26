import scala.sys.process._
import scala.language.postfixOps

import sbtwelcome._

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

lazy val `typicode-tyrian` =
  (project in file("."))
    .enablePlugins(ScalaJSPlugin)
    .settings( // Normal settings
      name         := "typicode-tyrian",
      version      := "0.0.1",
      scalaVersion := "3.2.0",
      organization := "pro.reiss",
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "tyrian-io"    % "0.6.0",
        "io.circe"        %%% "circe-parser" % "0.15.0-M1"
      ),
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
      scalafixOnCompile := true,
      semanticdbEnabled := true,
      semanticdbVersion := scalafixSemanticdb.revision,
      autoAPIMappings   := true
    )
    .settings( // Welcome message
      logo := "Tyrian Client for Typicode JSONPlaceholder (v" + version.value + ")",
      usefulTasks := Seq(
        UsefulTask("", "fastOptJS", "Rebuild the JS (use during development)"),
        UsefulTask("", "fullOptJS", "Rebuild the JS and optimise (use in production)")
      ),
      logoColor        := scala.Console.MAGENTA,
      aliasColor       := scala.Console.BLUE,
      commandColor     := scala.Console.CYAN,
      descriptionColor := scala.Console.WHITE
    )
