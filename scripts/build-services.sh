#!/bin/sh

cd "$(dirname "$0")" || exit
cd ..

services="./services/gateway ./services/author-service ./services/post-service ./services/post-metrics-service"
for service in ${services}
do
  eval "${service}/scripts/build.sh"
done
