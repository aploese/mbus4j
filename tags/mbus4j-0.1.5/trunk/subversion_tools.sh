#!/bin/sh
find ./ -name "*.java" -exec svn propset svn:keywords "Id"  {} \;
find ./ -name "*.betwixt" -exec svn propset svn:keywords "Id"  {} \;

echo Files without \$Id\:
find ./ -name "*.java" -exec grep -L \$Id {} \;
find ./ -name "*.betwixt" -exec grep -L \$Id {} \;

