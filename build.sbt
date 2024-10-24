name := """json2apex"""
organization := "com.johnforeland"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"


libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )