FROM openjdk:21-slim
ARG smart_service=target/*.jar
COPY ${smart_service} app.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","/app.jar"]