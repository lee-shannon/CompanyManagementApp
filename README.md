# company_management
This software allows a company to manage its employees and projects.

## Prerequisites

* JDK 8
* Maven
* NodeJS >= 18

## Department machines

You can code on the Linux departments machines by loading the course module.

Run `module load courses/cs415`.

This loads the correct version of Java, Maven, and NodeJS.

You should not be defining `JAVA_HOME` in your `~/.bashrc` or anywhere else.

# server
This repository contains the server for the CS-415 project. The server by default runs on port 4567.  You can check if the server is running correctly by accessing http://localhost:4567/helloworld.

`cd server/` before running any of the following commands.

## Start server

You can either run `mvn exec:exec` in the command line,

or through your IDE run the class Main directly.

## Package server into standalone JAR

Run `mvn package`.

## Run tests with JaCoCo's coverage

Run `mvn test`.

The coverage report will be inside `server/target/site/jacoco`.

Run the `python3 -m http.server 8000` command to see report.

Mutation testing command `mvn test-compile org.pitest:pitest-maven:mutationCoverage`
