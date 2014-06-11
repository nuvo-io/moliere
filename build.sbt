name		:= "moliere"

version		:= "0.3.0-SNAPSHOT"

organization 	:= "io.nuvo"

homepage :=  Some(new java.net.URL("http://nuvo.io"))

scalaVersion 	:= "2.11.1"

// This is used to fetch the jar for the DDS implementation (such as OpenSplice Mobile)
resolvers += "Vortex Snapshot Repo" at "https://dl.dropboxusercontent.com/u/19238968/devel/mvn-repo/snapshots"

libraryDependencies += "com.prismtech.cafe" % "cafe" % "2.0.0-SNAPSHOT"

libraryDependencies += "com.netflix.rxjava" % "rxjava-scala" % "0.16.0"

autoCompilerPlugins := true

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

scalacOptions += "-optimise"

scalacOptions += "-feature"

scalacOptions += "-language:postfixOps"

// This is used for publishing released into git-hub
publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath + "/Labs/mvn-repo/snapshots")))




