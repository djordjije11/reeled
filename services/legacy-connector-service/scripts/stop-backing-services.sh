#!/bin/sh

cd "$(dirname "$0")" || exit
cd ..

docker-compose -f docker-compose-backing-services.yml down
