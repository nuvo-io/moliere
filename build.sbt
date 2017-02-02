val jdkver =  if (System.getProperty("java.version").startsWith("1.7")) "-jdk7" else ""

name		:= s"moliere$jdkver"

version		:= "0.12.3-SNAPSHOT"

organization 	:= "io.nuvo"

homepage :=  Some(new java.net.URL("http://nuvo.io"))

scalaVersion 	:= "2.12.1"

val local = false

val localURI = "file:////Users/veda/.m2/repository"

val remoteURI = "https://dl.dropboxusercontent.com/u/19238968/devel/mvn-repo/vortex"

val repoURI = if (local) localURI else remoteURI

resolvers += "Vortex Snapshot Repo" at repoURI


libraryDependencies += "com.prismtech.cafe" % "cafe" % "2.3.1-SNAPSHOT"

autoCompilerPlugins := true

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

// scalacOptions += "-optimise"

scalacOptions += "-feature"

scalacOptions += "-language:postfixOps"

// This is used for publishing released into git-hub
publishTo := Some(Resolver.file("file",  new File( Path.userHome.absolutePath+"/hacking/zlab/mvn-repo/snapshots" )) )




