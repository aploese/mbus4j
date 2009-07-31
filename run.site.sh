#! /bin/sh
export MAVEN_OPTS="-Xmx1024M -XX:MaxPermSize=256M" &&
mvn clean install site
