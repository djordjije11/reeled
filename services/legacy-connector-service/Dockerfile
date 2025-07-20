FROM amazoncorretto:21
COPY build/libs/*-boot.jar /opt/legacy-connector-service/legacy-connector-service.jar
COPY entrypoint.sh /opt/legacy-connector-service/entrypoint.sh
WORKDIR /opt/legacy-connector-service
RUN chmod +x entrypoint.sh
ENTRYPOINT ["/opt/legacy-connector-service/entrypoint.sh"]
CMD ["java", "-jar", "legacy-connector-service.jar"]
EXPOSE 8080
