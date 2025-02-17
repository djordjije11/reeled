FROM amazoncorretto:21
COPY build/libs/*-boot.jar /opt/author-service/author-service.jar
COPY entrypoint.sh /opt/author-service/entrypoint.sh
WORKDIR /opt/author-service
RUN chmod +x entrypoint.sh
ENTRYPOINT ["/opt/author-service/entrypoint.sh"]
CMD ["java", "-jar", "author-service.jar"]
EXPOSE 8080
