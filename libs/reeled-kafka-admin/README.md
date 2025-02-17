# Reeled Kafka Admin

A java library containing a CLI app that can be used as an admin interface to Kafka for basic operations.

## Building & Publishing

Requirements:

* JDK 21 -
  suggested [Amazon Corretto 21](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/what-is-corretto-21.html)
* [non-unix only] Git BASH or other BASH emulator
* Create environment variables permanently /$HOME/.zshrc to access maven repository from Github Packages with the following properties.

```bash
export REELED_GH_PACKAGES_USERNAME=djordjije11
export REELED_GH_PACKAGES_TOKEN=`<token>`
```

### Build locally - without tests

```bash
./gradlew clean build -x test
```

### Build locally

```bash
./gradlew clean build
```

### Publish locally

1. Update the `artifactVersion` in `gradle.properties` file.
2. Build the project.
3. Publish the artifact by running:

```bash
./gradlew publish
```

## Usage

Run locally (requires a Kafka instance running on localhost:9092):

```shell script
java -jar build/libs/*.jar localhost:9092 topic1 topic2 topic3
```
