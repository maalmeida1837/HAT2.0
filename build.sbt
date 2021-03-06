

enablePlugins(JavaAppPackaging)

name := """The HAT"""
organization := """org.hatex"""
version := "2.0-SNAPSHOT"

scalaVersion := "2.11.8"

parallelExecution in Test := false

publishArtifact in Test := false

scalacOptions ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  //"-Xfatal-warnings", // Fail the compilation if there are any warnings.
  "-Xlint", // Enable recommended additional warnings.
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver.
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
  "-Ywarn-nullary-override", // Warn when non-nullary overrides nullary, e.g. def foo() over def foo.
  "-Ywarn-numeric-widen", // Warn when numerics are widened.
  "-language:reflectiveCalls",
  "-language:postfixOps", // Postfix operators allowed
  "-language:implicitConversions" // Allow for implicit conversions
)

logLevel := Level.Info

val akkaV = "2.4.7"
val sprayV = "1.3.3"
val specs2V = "3.3"
val slf4jV = "1.7.18"
val logbackV = "1.1.2"
val slickV = "3.1.1"
val slick_pgV = "0.14.2"
val jwtV = "4.22"
val postgresV = "9.4-1206-jdbc4"

lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "com.typesafe.slick" %% "slick" % slickV,
    "com.typesafe.slick" %% "slick-hikaricp" % slickV,
    "org.postgresql" % "postgresql" % postgresV,
    "com.github.tminglei" % "slick-pg_core_2.11" % slick_pgV,
    "com.github.tminglei" %% "slick-pg" % slick_pgV,
    "com.github.tminglei" %% "slick-pg_joda-time" % slick_pgV,
    "com.github.tminglei" %% "slick-pg_jts" % slick_pgV,
    "com.github.tminglei" % "slick-pg_spray-json_2.11" % slick_pgV,
    "joda-time" % "joda-time" % "2.9.2",
    "org.joda" % "joda-convert" % "1.8",
    "com.vividsolutions" % "jts" % "1.13",
    "org.slf4j" % "slf4j-api" % slf4jV,
    "ch.qos.logback" % "logback-core" % logbackV,
    "ch.qos.logback" % "logback-classic" % logbackV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "com.typesafe" % "config" % "1.3.0",
    "com.zaxxer" % "HikariCP" % "2.4.4",
    "com.typesafe.akka" %% "akka-http-core" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV
  )
)

// code generation task
lazy val gentables = taskKey[Seq[File]]("Slick Code generation")

lazy val codegen = (project in file("codegen")).
  settings(commonSettings: _*).
  settings(
    name := "codegen",
    libraryDependencies ++= List(
      "com.typesafe.slick" %% "slick-codegen" % slickV
    ),
    gentables := {
      val main = Project("root", file("."))
      val outputDir = (main.base.getAbsoluteFile / "src/main/scala").getPath
      streams.value.log.info("Output directory for codegen: " + outputDir.toString)
      val pkg = "hatdex.hat.dal"
      streams.value.log.info("Dependency classpath: " + dependencyClasspath.toString)
      (runner in Compile).value.run("hatdex.hat.dal.CustomizedCodeGenerator", (dependencyClasspath in Compile).value.files, Array(outputDir, pkg), streams.value.log)
      val fname = outputDir + "/" + pkg.replace('.', '/') + "/Tables.scala"
      Seq(file(fname))
    },
    cleanFiles <+= baseDirectory { base => base / "../src/main/scala/hatdex/hat/dal/Tables.scala" }
  )

//slick <<= slickCodeGenTask
//
//sourceGenerators in Compile <+= slickCodeGenTask
//
//lazy val slick = TaskKey[Seq[File]]("gen-tables")
//lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
//    val main = Project("root", file("."))
//    val outputDir = (main.base.getAbsoluteFile / "src/main/scala").getPath
//    val username = "hat20"
//    val password = "pa55w0rd"
//    val url = "jdbc:postgresql://localhost/hat20"
//    val jdbcDriver = "org.postgresql.Driver"
//    val slickDriver = "slick.driver.PostgresDriver"
//    val pkg = "hatdex.hat.dal"
//    toError(r.run("hatdex.hat.dal.CustomizedCodeGenerator", cp.files, Array(outputDir, pkg, slickDriver, jdbcDriver, url, username, password), s.log))
//    val fname = outputDir + "/" + pkg.replace('.', '/') + "/Tables.scala"
//    Seq(file(fname))
//  }

lazy val core = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "root",
    libraryDependencies ++= List(
      "io.spray" %% "spray-can" % sprayV,
      "io.spray" %% "spray-routing-shapeless2" % sprayV,
      "com.typesafe.akka" %% "akka-actor" % akkaV,
      "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
      "io.spray" %% "spray-testkit" % sprayV % "test",
      "org.specs2" % "specs2-core_2.11" % specs2V % "test",
      "org.specs2" % "specs2_2.11" % specs2V % "test",
      "io.spray" %% "spray-json" % "1.3.2",
      "org.mindrot" % "jbcrypt" % "0.3m",
      "com.nimbusds" % "nimbus-jose-jwt" % jwtV,
      "org.bouncycastle" % "bcprov-jdk16" % "1.46",
      "org.apache.commons" % "commons-email" % "1.4",
      "org.liquibase" % "liquibase-maven-plugin" % "3.5.1",
      "org.pegdown" % "pegdown" % "1.6.0"
    ),
    gentables := {
      val main = Project("root", file("."))
      val outputDir = (main.base.getAbsoluteFile / "src/main/scala").getPath
      streams.value.log.info("Output directory for codegen: " + outputDir.toString)
      val pkg = "hatdex.hat.dal"
      (runner in Compile).value.run("hatdex.hat.dal.CustomizedCodeGenerator", (dependencyClasspath in Compile).value.files, Array(outputDir, pkg), streams.value.log)
      val fname = outputDir + "/" + pkg.replace('.', '/') + "/Tables.scala"
      Seq(file(fname))
    },
    scoverage.ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := "hatdex.hat.dal",
    publishArtifact in(Compile, packageDoc) := false
  )
  .dependsOn("codegen")
  .enablePlugins(SbtWeb)
  .enablePlugins(SbtSassify)
    .settings(
      pipelineStages := Seq(uglify),
      sourceDirectory in Assets := baseDirectory.value / "src" / "main" / "assets",
      fullClasspath in reStart += baseDirectory.value / "src" / "main" / "assets",
      javaOptions in reStart += "-Xmx500m",
      javaOptions in reStart += "-Xms100m"
    )
  .enablePlugins(SbtTwirl)
  .settings(
    aggregate in update := false,
    sourceDirectories in(Compile, TwirlKeys.compileTemplates) := (unmanagedSourceDirectories in Compile).value,
    testOptions in Test += Tests.Argument(TestFrameworks.Specs2, "exclude", "REMOTE")
  )



//sourceDirectories in (Compile, TwirlKeys.compileTemplates) := (unmanagedSourceDirectories in Compile).value

resolvers ++= Seq(
  "scalaz.bintray" at "http://dl.bintray.com/scalaz/releases",
  "scoverage-bintray" at "https://dl.bintray.com/sksamuel/sbt-plugins/",
  "Atlassian Releases" at "https://maven.atlassian.com/public/"
)
