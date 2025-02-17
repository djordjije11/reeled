#!/bin/sh

cd "$(dirname "$0")" || exit
cd ..

docker-compose -f docker-compose-all-services.yml pull
docker-compose -f docker-compose-all-services.yml up --build -d

scripts/wait-and-setup-backing-services.sh
