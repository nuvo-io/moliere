val ospl = "ospl"

val cafe = "cafe"

val dds = ospl

name		:= s"moliere-$dds"

version		:= "0.5.0-SNAPSHOT"

organization 	:= "io.nuvo"

homepage :=  Some(new java.net.URL("http://nuvo.io"))

scalaVersion 	:= "2.11.6"

// This is used to fetch the jar for the DDS implementation (such as OpenSplice Mobile)

//resolvers +=  if (dds == cafe) "Vortex Snapshot Repo" at "https://dl.dropboxusercontent.com/u/19238968/devel/mvn-repo/vortex" else ""
//
//  libraryDependencies += "com.prismtech.cafe" % "cafe" % "2.1.0p1-SNAPSHOT"
//}

// resolvers += "vortex repo" at "file://"+Path.userHome.absolutePath+"/.m2/repository"




autoCompilerPlugins := true

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

scalacOptions += "-optimise"

scalacOptions += "-feature"

scalacOptions += "-language:postfixOps"

// This is used for publishing released into git-hub
publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath + "/xlabs/nuvo/mvn-repo/snapshots")))




