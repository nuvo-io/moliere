name		:= "moliere"

version		:= "0.1.1-SNAPSHOT"

organization 	:= "io.nuvo"

homepage :=  Some(new java.net.URL("http://nuvo.io"))

scalaVersion 	:= "2.10.3"

// This is used to fetch the jar for the DDS implementation (such as OpenSplice Mobile)
resolvers += "Local Maven Repo" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"

libraryDependencies += "org.opensplice.mobile" % "ospl-mobile" % "1.1.0"

autoCompilerPlugins := true

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

scalacOptions += "-optimise"

scalacOptions += "-feature"

scalacOptions += "-language:postfixOps"

// This is used for publishing released into git-hub
publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath + "/Labs/mvn-repo/snapshots")))




