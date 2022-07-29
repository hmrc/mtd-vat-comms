/*
 * Copyright 2022 HM Revenue & Customs
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
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName = "mtd-vat-comms"

lazy val appDependencies: Seq[ModuleID] = compile ++ test() ++ tmpMacWorkaround()

val compile = Seq(
  "uk.gov.hmrc.mongo" %% "hmrc-mongo-work-item-repo-play-28" % "0.68.0",
  "uk.gov.hmrc"       %% "bootstrap-backend-play-28"         % "5.24.0",
  "com.typesafe.play" %% "play-iteratees"                    % "2.6.1"
)

def test(scope: String = "test,it"): Seq[ModuleID] = Seq(
  "uk.gov.hmrc"            %% "bootstrap-test-play-28"       % "5.24.0"            % scope,
  "uk.gov.hmrc.mongo"      %% "hmrc-mongo-test-play-28"      % "0.68.0"            % scope,
  "org.pegdown"            %  "pegdown"                      % "1.6.0"             % scope,
  "org.scalatestplus"      %% "mockito-3-4"                  % "3.2.10.0"          % scope,
  "com.github.tomakehurst" %  "wiremock-jre8"                % "2.26.3"            % scope,
  "org.mockito"            %  "mockito-core"                 % "2.24.5"            % scope,
  "org.scalacheck"         %% "scalacheck"                   % "1.14.0"            % scope,
  "org.scalamock"          %% "scalamock-scalatest-support"  % "3.6.0"             % scope,
  "org.jsoup"              %  "jsoup"                        % "1.13.1"            % scope
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
  .settings(publishingSettings: _*)
  .settings(majorVersion := 0)
  .settings(defaultSettings(): _*)
  .settings(
    scalaVersion := "2.12.15",
    PlayKeys.playDefaultPort := 9579,
    libraryDependencies ++= appDependencies,
    retrieveManaged := true,
    routesGenerator := InjectedRoutesGenerator,
    resourceDirectory in Test := baseDirectory.value / "test" / "resources"
  )
  .configs(IntegrationTest)
  .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
  .settings(
    Keys.fork in IntegrationTest := false,
    unmanagedSourceDirectories in IntegrationTest := (baseDirectory in IntegrationTest)(base => Seq(base / "it")).value,
    resourceDirectory in IntegrationTest := baseDirectory.value / "test" / "resources",
    addTestReportOption(IntegrationTest, "int-test-reports"),
    testGrouping in IntegrationTest := oneForkedJvmPerTest((definedTests in IntegrationTest).value),
    parallelExecution in IntegrationTest := false
  )

def tmpMacWorkaround(): Seq[ModuleID] =
  if (sys.props.get("os.name").fold(false)(_.toLowerCase.contains("mac")))
    Seq("org.reactivemongo" % "reactivemongo-shaded-native" % "0.16.1-osx-x86-64" % "runtime,test,it")
  else Seq()
