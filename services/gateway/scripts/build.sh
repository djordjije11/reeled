#!/bin/sh

cd "$(dirname "$0")" || exit
cd ..

sh gradlew clean build
result=$?
exit ${result}
