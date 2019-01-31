# MTD VAT Comms

[![Build Status](https://travis-ci.org/hmrc/mtd-vat-comms.svg)](https://travis-ci.org/hmrc/mtd-vat-comms) [ ![Download](https://api.bintray.com/packages/hmrc/releases/mtd-vat-comms/images/download.svg) ](https://bintray.com/hmrc/releases/mtd-vat-comms/_latestVersion)

## Summary

This is the repository for MTD VAT Comms

This service receives and processes events from MTD services.

## Requirements

This service is written in [Scala](http://www.scala-lang.org/) and [Play](http://playframework.com/), so needs at least a [JRE](https://www.java.com/en/download/) to run.

## Running the application

In order to run this microservice, you must have SBT installed. You should be able to start the application using:

`sbt "run 9579"`

## Testing

Use the following command to run unit and integration tests:

`sbt test it:test`

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")
