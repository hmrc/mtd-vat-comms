# MTD VAT Comms

[![Build Status](https://travis-ci.org/hmrc/mtd-vat-comms.svg)](https://travis-ci.org/hmrc/mtd-vat-comms) [ ![Download](https://api.bintray.com/packages/hmrc/releases/mtd-vat-comms/images/download.svg) ](https://bintray.com/hmrc/releases/mtd-vat-comms/_latestVersion)

## Summary

This is the repository for MTD VAT Comms.

A batch job is run every weekday by ETMP and this sends the recently approved/rejected VAT change requests to our microservice (mtd-vat-comms). The request events give us some basic information such as the user’s VRN and a BP Contact Number, which we then use to call DES API#2 for further detailed information about the change.

So for example, if the event is a user changing their email address, API#2 gives us information such as what their original email address was, what the new one is, the “template ID” of the change (we use this to tell us whether the change was approved or rejected, and whether the change was requested by an agent), their contact preference, business name, etc. We use this information to determine what type of message to construct, if we need to send an email in addition to a secure message (e.g. agent gets an email, client gets a secure message), and to fill in the blanks in our secure message html templates.

We send our secure messages as encoded HTML to the microservice “message”, and our email requests to the microservice “email” where we specify a template ID which they will render for us. The secure messages appear in the user’s Business Tax Account messages inbox.

The service works with databases, queues, schedulers, and handles errors

## Requirements

This service is written in [Scala](http://www.scala-lang.org/) and [Play](http://playframework.com/), so needs at least a [JRE](https://www.java.com/en/download/) to run.

## Running the application

In order to run this microservice, you must have SBT installed. You should be able to start the application using:

`./run.sh`

## Testing

Use the following command to run unit and integration tests:

`sbt test it:test`

## License

This code is open source software licensed under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html)
