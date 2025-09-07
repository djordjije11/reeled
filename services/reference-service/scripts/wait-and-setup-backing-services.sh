#!/bin/sh

cd "$(dirname "$0")" || exit
cd ..

if [ -z "$REELED_GH_PACKAGES_READ_TOKEN" ]; then
    echo "Error: GitHub Packages token not set. Please set the REELED_GH_PACKAGES_READ_TOKEN environment variable."
    exit 1
fi
