import org.scalajs.linker.interface.{ModuleKind, ModuleSplitStyle}

lazy val V = new {
  val scala = "3.7.2"
  val tyrian = "0.14.0"
  val catsEffect = "3.6.3"
  val scalaJsDom = "2.8.1"
  val tapir = "1.11.42"
  val sttpClient = "3.9.8"
  val circe = "0.14.14"
  val scalaJavaTime = "2.6.0"
}

ThisBuild / organization := "$organization$"
ThisBuild / scalaVersion  := V.scala
ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "utf-8",
  "-unchecked"
)

lazy val commonSettings = Seq(
  scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) }
)

lazy val shared = (project in file("modules/shared"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings)
  .settings(
    name := "$name$-shared",
    libraryDependencies ++= Seq(
      // Tapir endpoint model + JSON codecs (shared with frontend)
      "com.softwaremill.sttp.tapir" %%% "tapir-core"        % V.tapir,
      "com.softwaremill.sttp.tapir" %%% "tapir-json-circe"  % V.tapir,
      // Circe JSON core (frontend uses parser too)
      "io.circe" %%% "circe-core"    % V.circe,
      // Some Tapir features rely on java.time types on JS
      "io.github.cquiroz" %%% "scala-java-time" % V.scalaJavaTime
    )
  )

lazy val frontend = (project in file("modules/frontend"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(shared)
  .settings(commonSettings)
  .settings(
    name := "$name$-frontend",
    libraryDependencies ++= Seq(
      "io.indigoengine" %%% "tyrian"      % V.tyrian,
      "io.indigoengine" %%% "tyrian-io"   % V.tyrian,
      "org.typelevel"   %%% "cats-effect" % V.catsEffect,
      "org.scala-js"    %%% "scalajs-dom" % V.scalaJsDom,
      // Tapir client + sttp client (Fetch backend) + JSON
      "com.softwaremill.sttp.tapir" %%% "tapir-sttp-client" % V.tapir,
      "com.softwaremill.sttp.client3" %%% "core"             % V.sttpClient,
      "io.circe" %%% "circe-parser" % V.circe
    ),
    // Run Scala.js module's main initializer (triggered by `import 'scalajs:frontend.js'`)
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= { _.withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("$package$.frontend"))) }
  )

lazy val worker = (project in file("modules/worker"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(shared)
  .settings(commonSettings)
  .settings(
    name := "$name$-worker",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-effect" % V.catsEffect,
      "org.scala-js"  %%% "scalajs-dom" % V.scalaJsDom
    ),
    // We export a JS function for the Worker; no auto main
    scalaJSUseMainModuleInitializer := false
  )
