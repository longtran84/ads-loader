name := """play-java-rest-api-example"""

version := "2.6.x"

inThisBuild(
  List(
    scalaVersion := "2.12.3",
    dependencyOverrides := Set(
       "org.codehaus.plexus" % "plexus-utils" % "3.0.18",
       "com.google.code.findbugs" % "jsr305" % "3.0.1",
       "com.google.guava" % "guava" % "22.0",
       "com.typesafe.akka" %% "akka-stream" % "2.5.4",
       "com.typesafe.akka" %% "akka-actor" % "2.5.4"
    )
  )
)


lazy val GatlingTest = config("gatling") extend Test

lazy val root = (project in file(".")).enablePlugins(PlayJava, GatlingPlugin).configs(GatlingTest)
  .settings(inConfig(GatlingTest)(Defaults.testSettings): _*)
  .settings(
    scalaSource in GatlingTest := baseDirectory.value / "/gatling/simulation"
  )

libraryDependencies += guice
libraryDependencies += javaJpa
libraryDependencies += "com.h2database" % "h2" % "1.4.194"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.43"

libraryDependencies += "org.hibernate" % "hibernate-core" % "5.2.9.Final"
libraryDependencies += "io.dropwizard.metrics" % "metrics-core" % "3.2.1"
libraryDependencies += "com.palominolabs.http" % "url-builder" % "1.1.0"
libraryDependencies += "net.jodah" % "failsafe" % "1.0.3"

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.3.0" % Test
libraryDependencies += "io.gatling" % "gatling-test-framework" % "2.3.0" % Test

PlayKeys.externalizeResources := false

testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
libraryDependencies ++= Seq(
  "io.swagger" %% "swagger-play2" % "1.6.0",
  "org.webjars" %% "webjars-play" % "2.6.2",
  "io.springfox" % "springfox-swagger-ui" % "2.7.0",
  "org.apache.solr" % "solr-solrj" % "6.6.1"
  //"org.webjars" % "swagger-ui" % "3.2.0"
)

libraryDependencies ++= Seq(
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final" // replace by your jpa implementation
)

PlayKeys.externalizeResources := false