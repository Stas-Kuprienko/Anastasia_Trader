# ANASTASIA TRADER
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Spring_Framework_6-green?style=flat-square&logo=Spring&logoColor=green&label=%7C)
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Hibernate_6-steelblue?style=flat-square&logo=Hibernate&logoColor=yellow&label=%7C&labelColor=grey)
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-MySQL_8-lightblue?style=flat-square&logo=mysql&logoSize=auto&logoColor=white&label=%7C&labelColor=grey)
![Static Badge](https://img.shields.io/badge/https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-gRPC-mediumturquoise?style=flat-square&logo=java&logoColor=mediumturquoise&label=%3C-%3E|&labelColor=grey)
![Static Badge](https://img.shields.io/badge/%20https%3A%2F%2Fimg.shields.io%2Fbadge%2Fany_text-Telegram_API-blue?logo=telegram&label=%7C)

<img src="Basic_node/App_UI/src/main/webapp/style/hello.webp" style="max-width: 96px; width: 96px;">

## Сервис для торговли на бирже, с возможностью автоматической торговли по выбранным стратегиям.
### Проект состоит из трёх модулей (микросервисов) - основное приложение, сервис автоматической торговли и телеграм-бот.
***
***
## Стэк технологий
+ #### Java 21
+ #### Maven
+ #### Redis
+ #### Kafka (планирую подключить)
***
+ ### Basic node:
  + Spring Framework 6
      + WebMVC
      + Web Security
      + Data Redis
      + ORM
  + Hibernate 6
  + MySQL 8 
  + JSP + JSTL
  + gRPC Framework
  + Apache TomCat 11
***
+ ### Smart service:
  + Spring Boot 3
  + Spring gRPC starter
  + gRPC Framework
***
+ ### Telegram bot:
  + Spring Boot 3
    + Data JPA (Hibernate)
    + Security
  + Telegram bots 6
  + MySQL 8
***
***
 ### Установка проэкта:
 Клонирование репозитория `git clone https://github.com/Stas-Kuprienko/Anastasia_Trader.git`
 Сборка проекта производится через Maven.
 Потребуется прописать `application.properties` конфигурацию каждого модуля.
> __Basic node:__
>> + _api.id_ и _api.secretKey_ для JWT аутентификации
>> + _database.jpa.properties_ название файла конфигурации с MySQL

> __Smart service:__
>> + _api.id_ и _api.secretKey_ для JWT аутентификации
>> + _grpc.api.token_ токен для доступа к API Финам для получения данных стакана

> __Telegram bot:__
>> + _telegram.username_ название телеграм-бота
>> + _telegram.botToken_ токен телеграм-бота (нужно получить в @BotFather)
>> + _api.resource_ адрес сайта __Basic node__ приложения
>> + _api.id_ и _api.secretKey_ для JWT аутентификации
>> + конфигурировать mysql данные

 Для запуска __Basic node__ нужно скачать __Apache Tomcat 11__ и прописать в файл `/conf/context.xml` следующее:
 ```
     <Resource auth="Container"
              name="jdbc/anastasia"
              type="javax.sql.DataSource"
              driverClassName="com.mysql.cj.jdbc.Driver"
              url="${url}"
              username="${username}"
              password="${password}"
    />
 ```
 А так же __Basic node__ можно запустить через `Dockerfile`(нужно прописть образ MySql)
 
//TODO
