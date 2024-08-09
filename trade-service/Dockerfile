FROM tomcat:11.0.0-M21-jdk21
ADD ./trade-service-api/target/anastasia_api.war /usr/local/tomcat/webapps/
ADD ./trade-service-ui/target/anastasia.war /usr/local/tomcat/webapps/
ADD resources/context.xml /usr/local/tomcat/conf/
ADD resources/mysql-connector-j-8.3.0.jar /usr/local/tomcat/lib
EXPOSE 8080
CMD ["catalina.sh", "run"]