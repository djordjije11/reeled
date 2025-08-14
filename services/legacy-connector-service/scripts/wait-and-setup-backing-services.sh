#!/bin/sh

cd "$(dirname "$0")" || exit
cd ..

if [ -z "$REELED_GH_PACKAGES_READ_TOKEN" ]; then
    echo "Error: GitHub Packages token not set. Please set the REELED_GH_PACKAGES_READ_TOKEN environment variable."
    exit 1
fi

scripts/wait-for-services.sh localhost:8081 localhost:8082

# Create Kafka topics
curl -L https://maven.pkg.github.com/djordjije11/reeled/io.github.djordjije11.reeled.reeled-kafka-admin/1.0.0/reeled-kafka-admin-1.0.0.jar \
 -H "Authorization: Bearer $REELED_GH_PACKAGES_READ_TOKEN" \
 -o reeled-kafka-admin.jar
java -jar reeled-kafka-admin.jar localhost:9092 reeled-default-post-event reeled-default-author-event
java -jar reeled-kafka-admin.jar localhost:9093 reeled_legacy_default.public.post \
  reeled_legacy_default.public.author \
  reeled_legacy_default.public.post_category \
  reeled_legacy_default.public.author_type
rm -rf reeled-kafka-admin.jar
