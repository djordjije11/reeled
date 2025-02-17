#!/bin/sh

services=$*

for service in ${services}
do
    while true
    do
        HTTP=$(curl -s -o /dev/null --max-time 3 -w "%{http_code}" "${service}")
        if [ "$HTTP" = "200" ]
        then
            echo "service $service is up"
            break
        else
            echo "waiting for $service"
        fi
        sleep 1
    done
done
echo "all services up"
