#!/bin/sh
export MAVEN_OPTS="-Xmx2028M -XX:MaxPermSize=256M" &&
mvn -e site
