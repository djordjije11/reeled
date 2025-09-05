# Reeled

A repository containing Spring Boot libraries (`./libs/`) and independent microservices (`./services/`). Each library and service can be built and run
independently, with its own README.md file providing specific instructions. However, all services can also be started together by following the instructions
below.

## Running

Requirements:

* JDK 21 -
  suggested [Amazon Corretto 21](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/what-is-corretto-21.html)
* Docker & Compose - for running backing services like PostgreSQL, Apache Kafka...
* [non-unix only] Git BASH or other BASH emulator

### Run all services

Start all services by:

```bash
./scripts/start.sh
```

This will spin up all Spring Boot applications along with necessary backing services such as databases and message brokers.

After all services are up and ready, the console will display: `All Reeled services started successfully!`

Stop all services by:

```bash
./scripts/stop.sh
```

### Access Swagger UI

Once the services are successfully running, you can access the [Swagger UI](http://localhost:8080/swagger-ui/index.html) at
`/swagger-ui/index.html`. It provides a simple way to explore the API and send requests.
The Gateway Swagger UI provides access to all endpoints, while each service also exposes its own Swagger UI separately.

Services are available at the following ports:

- [Gateway](http://localhost:8080/swagger-ui/index.html) at port `8080`
- [Legacy](http://localhost:8084/swagger-ui/index.html) at port `8084`
- [Author Service](http://localhost:8085/swagger-ui/index.html) at port `8085`
- [Post Service](http://localhost:8086/swagger-ui/index.html) at port `8086`
- [Post Metrics Service](http://localhost:8087/swagger-ui/index.html) at port `8087`

### Additional notes

- Within the `.../src/test/resources/` directories of each service, you can find CSV files that can be used to populate the database with test data.
- For the use case of importing post metrics, a class
  `services/post-metrics-service/src/test/java/io/github/djordjije11/reeled/TestPostDailyPerformanceFileGenerator.java` can be used to generate a CSV file with
  random sample data for testing purposes.
- At `docker-compose-all-services.yml` you can find the configuration for all backing services used by the services.
