#!/bin/sh

cd "$(dirname "$0")" || exit
cd ..

docker-compose -f docker-compose-backing-services.yml pull
docker-compose -f docker-compose-backing-services.yml up -d

scripts/wait-and-setup-backing-services.sh
