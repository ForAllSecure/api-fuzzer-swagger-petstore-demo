#!/bin/bash

# Download the JaCoCo CLI if not present
test -f jacococli.jar || curl -o jacococli.jar https://repo1.maven.org/maven2/org/jacoco/jacoco-cli/0.8.7/jacoco-cli-0.8.7.jar

# Connect to the JaCoCo agent server and retrieve coverage data
curl -o coverage.exec "tcp://localhost:6300/jacoco/exec?destfile=coverage.exec"

# Optionally, perform further processing on the coverage data
# For example, generate a report using JaCoCo's command-line interface (CLI)
java -jar jacococli.jar report coverage.exec --html report


