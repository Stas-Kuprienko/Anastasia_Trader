FROM openjdk:21-slim
COPY target/smart-service-1.0.jar smart-service.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","/smart-service.jar"]