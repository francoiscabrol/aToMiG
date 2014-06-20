// Project name
name := "atomig"

// Project version
version := "0.1.0"

// Scala version
scalaVersion := "2.10.4"

// Add dependency on ScalaFX library from Maven repository
libraryDependencies += "org.scalafx" %% "scalafx" % "1.0.0-R8"

// Add dependency with the scala actor library
libraryDependencies <++= scalaVersion(v =>
  Seq("org.scala-lang" % "scala-actors" % v)
)

// Add dependency on JavaFX library based on JAVA_HOME variable
unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/jfxrt.jar"))

unmanagedJars in Compile += Attributed.blank(file(System.getenv("LEAP_HOME") + "/lib/*"))
