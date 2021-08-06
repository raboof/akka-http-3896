scalaVersion := "3.0.1"

val akkaVersion = "2.6.8"
val akkaHttpVersion = "10.2.6"

libraryDependencies ++= Seq(
  ("com.typesafe.akka" %% "akka-stream" % akkaVersion).cross(CrossVersion.for3Use2_13),
  ("com.typesafe.akka" %% "akka-http" % akkaHttpVersion).cross(CrossVersion.for3Use2_13),
)
