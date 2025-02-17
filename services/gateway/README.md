# Reeled Gateway

A Spring Boot service to manage Reeled API Gateway definition.

## Building & Running

Requirements:

* JDK 21 -
  suggested [Amazon Corretto 21](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/what-is-corretto-21.html)
* [non-unix only] Git BASH or other BASH emulator
* All internal services running

### Build locally

```bash
./scripts/build.sh
```

### Run locally

1. Start all internal services
2. Run the application by:

* starting the `GatewayServiceApplication` main class from the IDE
* or, start the spring boot application using the gradle task:
    ```bash
    ./gradlew bootRun
    ```

### Access Swagger UI

Once the application is successfully running, you can access the [Swagger UI](http://localhost:8080/swagger-ui/index.html) at
`/swagger-ui/index.html`.
It provides a simple way to explore the API and send requests.
