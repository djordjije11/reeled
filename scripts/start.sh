#!/bin/sh

cd "$(dirname "$0")" || exit
cd ..

scripts/build-services.sh
scripts/start-all-services.sh

printf "\n"
echo "All Reeled services started successfully!"
