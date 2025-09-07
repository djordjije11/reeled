FROM amazoncorretto:21
COPY build/libs/*-boot.jar /opt/reference-service/reference-service.jar
COPY entrypoint.sh /opt/reference-service/entrypoint.sh
WORKDIR /opt/reference-service
RUN chmod +x entrypoint.sh
ENTRYPOINT ["/opt/reference-service/entrypoint.sh"]
CMD ["java", "-jar", "reference-service.jar"]
EXPOSE 8080
