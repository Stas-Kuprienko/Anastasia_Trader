FROM openjdk:21-slim
ARG smart_service=target/*.jar
COPY ${smart_service} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]