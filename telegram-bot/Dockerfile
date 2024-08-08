FROM openjdk:21-slim
COPY target/telegram-bot-1.0.0.jar telegram-bot.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/telegram-bot.jar"]