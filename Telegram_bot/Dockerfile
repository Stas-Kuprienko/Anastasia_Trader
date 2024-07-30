FROM openjdk:21-slim
ARG telegram_bot=target/*.jar
COPY ${telegram_bot} app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]