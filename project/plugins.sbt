resolvers += Resolver.typesafeRepo("releases")
libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.0")
addSbtPlugin("io.spray" % "sbt-revolver" % "0.8.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")
addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.1.1")

addSbtPlugin("com.orrsella" % "sbt-stats" % "1.0.5")

addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.4.0")
addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.6")
addSbtPlugin("com.typesafe.sbt" % "sbt-uglify" % "1.0.3")
