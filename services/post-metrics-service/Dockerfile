FROM amazoncorretto:21
COPY build/libs/*-boot.jar /opt/post-metrics-service/post-metrics-service.jar
COPY entrypoint.sh /opt/post-metrics-service/entrypoint.sh
WORKDIR /opt/post-metrics-service
RUN chmod +x entrypoint.sh
ENTRYPOINT ["/opt/post-metrics-service/entrypoint.sh"]
CMD ["java", "-jar", "post-metrics-service.jar"]
EXPOSE 8080
