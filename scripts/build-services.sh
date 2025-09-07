#!/bin/sh

cd "$(dirname "$0")" || exit
cd ..

services="./services/reeled-legacy \
 ./services/gateway \
 ./services/author-service \
 ./services/post-service \
 ./services/post-metrics-service \
 ./services/legacy-connector-service \
 ./services/reference-service"

for service in ${services}
do
  eval "${service}/scripts/build.sh"
done
