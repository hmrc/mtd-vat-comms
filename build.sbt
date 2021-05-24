/*
 * Copyright 2021 HM Revenue & Customs
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

import play.core.PlayVersion
import play.sbt.routes.RoutesKeys
import sbt.Keys.testGrouping
import sbt.Tests.{Group, SubProcess}
import uk.gov.hmrc.DefaultBuildSettings.{addTestReportOption, defaultSettings, scalaSettings}
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName = "mtd-vat-comms"

lazy val appDependencies: Seq[ModuleID] = compile ++ test() ++ tmpMacWorkaround()

val compile = Seq(
  "uk.gov.hmrc" %% "simple-reactivemongo" % "7.29.0-play-26",
  "uk.gov.hmrc" %% "work-item-repo"       % "7.6.0-play-26",
  "uk.gov.hmrc" %% "bootstrap-backend-play-26"    % "2.24.0",
  "uk.gov.hmrc" %% "play-scheduling"      % "7.4.0-play-26"
)

def test(scope: String = "test,it"): Seq[ModuleID] = Seq(
  "uk.gov.hmrc"            %% "hmrctest"                     % "3.9.0-play-26"     % scope,
  "org.scalatest"          %% "scalatest"                    % "3.0.8"             % scope,
  "com.typesafe.play"      %% "play-test"                    % PlayVersion.current % scope,
  "org.pegdown"            %  "pegdown"                      % "1.6.0"             % scope,
  "org.scalatestplus.play" %% "scalatestplus-play"           % "3.1.0"             % scope,
  "com.github.tomakehurst" %  "wiremock-jre8"                % "2.27.1"            % scope,
  "org.mockito"            %  "mockito-core"                 % "2.24.5"            % scope,
  "org.scalacheck"         %% "scalacheck"                   % "1.14.0"            % scope,
  "org.scalamock"          %% "scalamock-scalatest-support"  % "3.6.0"             % scope,
  "org.jsoup"              %  "jsoup"                        % "1.13.1"            % scope
)

lazy val coverageSettings: Seq[Setting[_]] = {
  import scoverage.ScoverageKeys

  val excludedPackages = Seq(
    "<empty>",
    "Reverse.*",
    ".*standardError*.*",
    ".*govuk_wrapper*.*",
    ".*main_template*.*",
    "uk.gov.hmrc.BuildInfo",
    "app.*",
    "prod.*",
    "config.*",
    "testOnly.*",
    ".*feedback*.*",
    "views.html.*",
    "partials.*",
    "controllers..*Reverse.*"
  )

  Seq(
    ScoverageKeys.coverageExcludedPackages := excludedPackages.mkString(";"),
    ScoverageKeys.coverageMinimum := 95,
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
    scalaVersion := "2.12.11",
    PlayKeys.playDefaultPort := 9579,
    libraryDependencies ++= appDependencies,
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
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
