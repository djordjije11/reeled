FROM amazoncorretto:21
COPY build/libs/*-boot.jar /opt/reeled-legacy/reeled-legacy.jar
COPY entrypoint.sh /opt/reeled-legacy/entrypoint.sh
WORKDIR /opt/reeled-legacy
RUN chmod +x entrypoint.sh
ENTRYPOINT ["/opt/reeled-legacy/entrypoint.sh"]
CMD ["java", "-jar", "reeled-legacy.jar"]
EXPOSE 8080
