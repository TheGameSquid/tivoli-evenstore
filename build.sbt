name := "tivoli-eventstore"

version := "0.1"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.3.9",
	"net.fwbrasil" %% "activate-core" % "1.6",
	"net.fwbrasil" %% "activate-jdbc" % "1.6"
)

resolvers ++= Seq(
	"Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
	"Activate Snapshot Repository" at "http://repo1.maven.org/maven2/net/fwbrasil/"
)
