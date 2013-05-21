name		:= "moliere"

version		:= "0.0.1"

organization 	:= "io.nuvola"

homepage :=  Some(new java.net.URL("http://blog.nuvola.io"))

scalaVersion 	:= "2.10.1"

resolvers += "Local Maven Repo" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"

libraryDependencies += "org.opensplice.mobile" % "mobile-dds" % "1.0.1-SNAPSHOT"

libraryDependencies += "org.opensplice.mobile" % "mobile-dds-core" % "1.0.1-SNAPSHOT"

libraryDependencies += "org.opensplice.mobile" % "mobile-ddsi" % "1.0.1-SNAPSHOT"


autoCompilerPlugins := true

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

scalacOptions += "-optimise"

scalacOptions += "-feature"

scalacOptions += "-language:postfixOps"





