name := """pholi"""

version := "0.1.0"

organization := "tr.megevera"

scalaVersion := "2.11.4"

crossScalaVersions := Seq("2.10.4", "2.11.4")

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.7",
  "org.specs2" %% "specs2-core" % "2.4.11" % "test",
  "com.h2database" % "h2" % "1.4.182" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")

scalacOptions in Compile ++= Seq(Opts.compile.deprecation, Opts.compile.unchecked) ++ Opts.compile.encoding("utf8")
