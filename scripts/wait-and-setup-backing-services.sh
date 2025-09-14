#!/bin/sh

cd "$(dirname "$0")" || exit
cd ..

if [ -z "$REELED_GH_PACKAGES_READ_TOKEN" ]; then
    echo "Error: GitHub Packages token not set. Please set the REELED_GH_PACKAGES_READ_TOKEN environment variable."
    exit 1
fi

scripts/wait-for-services.sh localhost:8081 localhost:8082 localhost:8083

# Create Kafka topics
curl -L https://maven.pkg.github.com/djordjije11/reeled/io.github.djordjije11.reeled.reeled-kafka-admin/1.0.0/reeled-kafka-admin-1.0.0.jar \
 -H "Authorization: Bearer $REELED_GH_PACKAGES_READ_TOKEN" \
 -o reeled-kafka-admin.jar
java -jar reeled-kafka-admin.jar localhost:9092 reeled-default-post-event reeled-default-author-event
java -jar reeled-kafka-admin.jar localhost:9093 reeledlegacy.public.post \
  reeledlegacy.public.author \
  reeledlegacy.public.post_category
rm -rf reeled-kafka-admin.jar

# Setup Debezium Postgres connector
curl -i -X POST -H "Accept: application/json" -H "Content-Type: application/json" localhost:8083/connectors/ --data @- <<'EOF'
{
  "name": "reeled-debezium-postgres-connector",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
    "plugin.name": "pgoutput",
    "database.hostname": "reeled-legacy-db",
    "database.port": "5432",
    "database.dbname": "reeled_legacy",
    "database.user": "reeled_legacy",
    "database.password": "reeled_legacy_password",
    "database.server.name": "reeledlegacy",
    "table.include.list": "public.post,public.author,public.post_category",
    "key.converter": "io.confluent.connect.avro.AvroConverter",
    "key.converter.schema.registry.url": "http://legacy-schema-registry:8082",
    "key.converter.key.subject.name.strategy": "io.confluent.kafka.serializers.subject.TopicRecordNameStrategy",
    "value.converter": "io.confluent.connect.avro.AvroConverter",
    "value.converter.schema.registry.url": "http://legacy-schema-registry:8082",
    "value.converter.value.subject.name.strategy": "io.confluent.kafka.serializers.subject.TopicRecordNameStrategy"
  }
}
EOF
