/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import play.sbt.routes.RoutesKeys
import sbt.Keys.testGrouping
import sbt.Tests.{Group, SubProcess}
import uk.gov.hmrc.DefaultBuildSettings.{addTestReportOption, defaultSettings, scalaSettings}

val appName = "mtd-vat-comms"

lazy val appDependencies: Seq[ModuleID] = compile ++ test()

val compile = Seq(
  "uk.gov.hmrc.mongo" %% "hmrc-mongo-work-item-repo-play-28" % "1.1.0",
  "uk.gov.hmrc"       %% "bootstrap-backend-play-28"         % "7.15.0"
)

def test(scope: String = "test,it"): Seq[ModuleID] = Seq(
  "uk.gov.hmrc"            %% "bootstrap-test-play-28"       % "7.15.0"            % scope,
  "uk.gov.hmrc.mongo"      %% "hmrc-mongo-test-play-28"      % "1.1.0"             % scope,
  "org.scalatestplus"      %% "mockito-3-4"                  % "3.2.10.0"          % scope,
  "org.scalamock"          %% "scalamock"                    % "5.2.0"             % scope,
  "org.specs2"             %% "specs2-core"                  % "4.19.1"            % scope
)

lazy val coverageSettings: Seq[Setting[_]] = {
  import scoverage.ScoverageKeys

  val excludedPackages = Seq(
    "<empty>",
    ".*Reverse.*",
    "app.*",
    "prod.*",
    "config.*",
    "testOnly.*",
    "views.html.*"
  )

  Seq(
    ScoverageKeys.coverageExcludedPackages := excludedPackages.mkString(";"),
    ScoverageKeys.coverageMinimumStmtTotal := 95,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}

def oneForkedJvmPerTest(tests: Seq[TestDefinition]): Seq[Group] = tests map {
  test => Group(test.name, Seq(test), SubProcess(
    ForkOptions().withRunJVMOptions(Vector("-Dtest.name=" + test.name, "-Dlogger.resource=logback-test.xml"))
  ))
}

RoutesKeys.routesImport := Seq.empty

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(coverageSettings: _*)
  .settings(scalaSettings: _*)
  .settings(majorVersion := 0)
  .settings(defaultSettings(): _*)
  .settings(
    scalaVersion := "2.13.8",
    PlayKeys.playDefaultPort := 9579,
    libraryDependencies ++= appDependencies,
    retrieveManaged := true,
    routesGenerator := InjectedRoutesGenerator,
    Test / resourceDirectory := baseDirectory.value / "test" / "resources",
    scalacOptions ++= Seq("-Wconf:cat=unused-imports&site=.*views.html.*:s")
  )
  .configs(IntegrationTest)
  .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
  .settings(
    IntegrationTest / Keys.fork := false,
    IntegrationTest / unmanagedSourceDirectories := (IntegrationTest / baseDirectory)(base => Seq(base / "it")).value,
    IntegrationTest / resourceDirectory := baseDirectory.value / "test" / "resources",
    IntegrationTest / testGrouping  := oneForkedJvmPerTest((IntegrationTest / definedTests).value),
    IntegrationTest / parallelExecution := false,
    addTestReportOption(IntegrationTest, "int-test-reports")
  )
