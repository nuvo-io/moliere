name		:= "moliere"

version		:= "0.1.0-SNAPSHOT"

organization 	:= "io.nuvo"

homepage :=  Some(new java.net.URL("http://nuvo.io"))

scalaVersion 	:= "2.10.2"

seq(githubRepoSettings: _*)

localRepo := Path.userHome /  "github" / "repo"

githubRepo := "git@github.com:nuvo-io/mvn-repo.git"


// This is used to fetch the jar for the DDS implementation (such as OpenSplice Mobile)
resolvers += "Local Maven Repo" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"

libraryDependencies += "org.opensplice.mobile" % "ospl-mobile" % "1.1.1-SNAPSHOT"

autoCompilerPlugins := true

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

scalacOptions += "-optimise"

scalacOptions += "-feature"

scalacOptions += "-language:postfixOps"





