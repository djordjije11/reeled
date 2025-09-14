#!/bin/sh

cd "$(dirname "$0")" || exit
cd ..

if [ -z "$REELED_GH_PACKAGES_READ_TOKEN" ]; then
    echo "Error: GitHub Packages token not set. Please set the REELED_GH_PACKAGES_READ_TOKEN environment variable."
    exit 1
fi

scripts/wait-for-services.sh localhost:8081 localhost:8083

# Create Kafka Debezium topics
curl -L https://maven.pkg.github.com/djordjije11/reeled/io.github.djordjije11.reeled.reeled-kafka-admin/1.0.0/reeled-kafka-admin-1.0.0.jar \
 -H "Authorization: Bearer $REELED_GH_PACKAGES_READ_TOKEN" \
 -o reeled-kafka-admin.jar
java -jar reeled-kafka-admin.jar localhost:9092 reeledlegacy.public.post \
  reeledlegacy.public.author \
  reeledlegacy.public.post_category \
  reeledlegacy.transaction
rm -rf reeled-kafka-admin.jar

# Setup Debezium Postgres connector
curl -i -X POST -H "Accept: application/json" -H "Content-Type: application/json" localhost:8083/connectors/ --data @- <<'EOF'
{
  "name": "reeled-debezium-postgres-connector",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
    "plugin.name": "pgoutput",
    "database.hostname": "db",
    "database.port": "5432",
    "database.dbname": "reeled_legacy",
    "database.user": "reeled_legacy",
    "database.password": "reeled_legacy_password",
    "database.server.name": "reeledlegacy",
    "table.include.list": "public.post,public.author,public.post_category",
    "provide.transaction.metadata": "true",
    "key.converter": "io.confluent.connect.avro.AvroConverter",
    "key.converter.schema.registry.url": "http://schema-registry:8081",
    "key.converter.key.subject.name.strategy": "io.confluent.kafka.serializers.subject.TopicRecordNameStrategy",
    "value.converter": "io.confluent.connect.avro.AvroConverter",
    "value.converter.schema.registry.url": "http://schema-registry:8081",
    "value.converter.value.subject.name.strategy": "io.confluent.kafka.serializers.subject.TopicRecordNameStrategy"
  }
}
EOF
