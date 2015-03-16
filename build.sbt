name		:= "moliere"

version		:= "0.6.3-SNAPSHOT"

organization 	:= "io.nuvo"

homepage :=  Some(new java.net.URL("http://nuvo.io"))

scalaVersion 	:= "2.11.5"

// This is used to fetch the jar for the DDS implementation (such as OpenSplice Mobile)
resolvers += "Vortex Snapshot Repo" at "https://dl.dropboxusercontent.com/u/19238968/vortex/mvn-repo"

libraryDependencies += "com.prismtech.cafe" % "cafe" % "2.1.0p1-SNAPSHOT"

autoCompilerPlugins := true

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

scalacOptions += "-optimise"

scalacOptions += "-feature"

scalacOptions += "-language:postfixOps"

// This is used for publishing released into git-hub
publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath + "/xlab/nuvo/mvn-repo/snapshots")))




