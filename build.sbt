import sbt.Keys._
import sbt._
import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin._

name := "Assignment"
organization := "edubigdata"
version := "1.0"

scalaVersion := "2.10.4"

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

retrieveManaged := true

assemblySettings

resolvers += "ArtifactoryESDev" at "http://esrepo.int.westgroup.com/artifactory/libs-release-local/"
resolvers += "CloudEra" at "https://repository.cloudera.com/artifactory/cloudera-repos/"
resolvers += "Commons-Cli" at "https://mvnrepository.com/artifact/commons-cli/commons-cli"

libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.5.1" excludeAll(ExclusionRule(organization = "org.apache.logging"))
libraryDependencies += "org.apache.spark" % "spark-sql_2.10" % "1.5.1"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "1.5.1"
libraryDependencies += "org.apache.spark" % "spark-yarn_2.10" % "1.5.1"

libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.7.0"

libraryDependencies += "commons-cli" % "commons-cli" % "1.3"
libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.6.2"

libraryDependencies += "junit" % "junit" % "4.12"
libraryDependencies += "com.google.inject" % "guice" % "3.0"

mainClass in assembly := Some("com.edubigdata.examples.deo.DeoRunner")


    