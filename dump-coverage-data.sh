#!/bin/bash

# Delete previous report
rm -rf report

# Delete previous coverage data
rm -f coverage.exec

# Download JaCoCo CLI  if not already present
test -f jacococli.jar || curl -o jacococli.jar https://repo1.maven.org/maven2/org/jacoco/org.jacoco.cli/0.8.6/org.jacoco.cli-0.8.6-nodeps.jar

# Optionally, perform further processing on the coverage data
# For example, generate a report using JaCoCo's command-line interface (CLI)
java -jar jacococli.jar dump --address localhost --port 6300 --destfile coverage.exec

# Generate a report from the coverage data
java -jar jacococli.jar report coverage.exec --classfiles target/classes/ --sourcefiles src/main/java --name "PetStore" --html report

open report/index.html