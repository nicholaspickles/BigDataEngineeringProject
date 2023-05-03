name := "cs5614_finalProject_1"

version := "0.1.0-SNAPSHOT"

// do not use with scalatest -- scala 2.13.10
scalaVersion := "2.12.10"

//libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.15"
libraryDependencies += "org.scoverage" % "sbt-scoverage_2.12_1.0" % "2.0.7"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"

coverageExcludedFiles := ".*\\/WordCount;.*\\/getSuspicious"
coverageHighlighting := true
coverageEnabled := true
coverageFailOnMinimum := false
coverageMinimumStmtTotal := 70
coverageMinimumBranchTotal := 70
coverageMinimumStmtPerPackage := 70
coverageMinimumBranchPerPackage := 70
coverageMinimumStmtPerFile := 70
coverageMinimumBranchPerFile := 70

