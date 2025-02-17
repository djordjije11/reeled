FROM amazoncorretto:21
COPY build/libs/*-boot.jar /opt/gateway/gateway.jar
COPY entrypoint.sh /opt/gateway/entrypoint.sh
WORKDIR /opt/gateway
RUN chmod +x entrypoint.sh
ENTRYPOINT ["/opt/gateway/entrypoint.sh"]
CMD ["java", "-jar", "gateway.jar"]
EXPOSE 8080
