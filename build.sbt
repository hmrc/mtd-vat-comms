/*
 * Copyright 2019 HM Revenue & Customs
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
import sbt.Keys.testGrouping
import sbt.Tests.{Group, SubProcess}
import uk.gov.hmrc.DefaultBuildSettings.{addTestReportOption, defaultSettings, scalaSettings}
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName = "mtd-vat-comms"

lazy val appDependencies: Seq[ModuleID] = compile ++ test()

val compile = Seq(
  ws,
  "uk.gov.hmrc" %% "simple-reactivemongo" % "7.20.0-play-25",
  "uk.gov.hmrc" %% "work-item-repo"       % "6.6.0-play-25",
  "uk.gov.hmrc" %% "bootstrap-play-25"    % "4.13.0",
  "uk.gov.hmrc" %% "play-scheduling"      % "6.0.0"
)

def test(scope: String = "test,it"): Seq[ModuleID] = Seq(
  "uk.gov.hmrc"            %% "hmrctest"                     % "3.9.0-play-25"     % scope,
  "org.scalatest"          %% "scalatest"                    % "3.0.6"             % scope,
  "com.typesafe.play"      %% "play-test"                    % PlayVersion.current % scope,
  "org.pegdown"            %  "pegdown"                      % "1.6.0"             % scope,
  "org.scalatestplus.play" %% "scalatestplus-play"           % "2.0.1"             % scope,
  "com.github.tomakehurst" %  "wiremock"                     % "2.21.0"            % scope,
  "org.mockito"            %  "mockito-core"                 % "2.24.5"            % scope,
  "org.scalacheck"         %% "scalacheck"                   % "1.14.0"            % scope,
  "org.scalamock"          %% "scalamock-scalatest-support"  % "3.6.0"             % scope,
  "uk.gov.hmrc"            %% "reactivemongo-test"           % "4.15.0-play-25"     % scope,
  "org.jsoup"              %  "jsoup"                        % "1.11.3"            % scope
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
    "partials.*"
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
    ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name, "-Dlogger.resource=logback-test.xml"))
  ))
}

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala,SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
  .settings(coverageSettings: _*)
  .settings(scalaSettings: _*)
  .settings(publishingSettings: _*)
  .settings(majorVersion := 0)
  .settings(defaultSettings(): _*)
  .settings(
    scalaVersion := "2.11.11",
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

def apply() = compile ++ test() ++ tmpMacWorkaround()
