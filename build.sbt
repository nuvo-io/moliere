name		:= "moliere"

version		:= "0.0.1-SNAPSHOT"

organization 	:= "io.nuvo"

homepage :=  Some(new java.net.URL("http://nuvo.io"))

scalaVersion 	:= "2.10.1"

seq(githubRepoSettings: _*)

localRepo := Path.userHome / "Veda" /  "github" / "repo"

githubRepo := "git@github.com:nuvo-io/mvn-repo.git"


// This is used to fetch the jar for the DDS implementation (such as OpenSplice Mobile)
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





