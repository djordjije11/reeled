# Reeled Legacy

A Spring Boot monolithic application.

## Building & Running

Requirements:

* JDK 21 -
  suggested [Amazon Corretto 21](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/what-is-corretto-21.html)
* Docker & Compose - for running backing services like PostgreSQL...
* [non-unix only] Git BASH or other BASH emulator

### Build locally - without tests

```bash
./gradlew clean build -x test
```

### Build locally

```bash
./scripts/build.sh
```

### Run locally

1. Start the backing services:
    ```bash
    ./scripts/start-backing-services.sh
    ```
2. Run the application by:

* starting the `ReeledLegacyApplication` main class from the IDE
* or, start the spring boot application using the gradle task:
    ```bash
    ./gradlew bootRun
    ```

### Access Swagger UI

Once the application is successfully running, you can access the [Swagger UI](http://localhost:8080/swagger-ui/index.html) at
`/swagger-ui/index.html`.
It provides a simple way to explore the API and send requests.
