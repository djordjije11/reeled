FROM amazoncorretto:21
COPY build/libs/*-boot.jar /opt/post-service/post-service.jar
COPY entrypoint.sh /opt/post-service/entrypoint.sh
WORKDIR /opt/post-service
RUN chmod +x entrypoint.sh
ENTRYPOINT ["/opt/post-service/entrypoint.sh"]
CMD ["java", "-jar", "post-service.jar"]
EXPOSE 8080
