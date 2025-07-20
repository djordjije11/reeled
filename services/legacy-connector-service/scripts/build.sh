#!/bin/sh

cd "$(dirname "$0")" || exit
cd ..

scripts/start-backing-services.sh
sh gradlew clean build
result=$?
scripts/stop-backing-services.sh
exit ${result}
