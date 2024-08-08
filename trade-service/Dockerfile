FROM tomcat:11.0.0-M21-jdk21
ADD ./trade-service-api/target/anastasia_api.war /usr/local/tomcat/webapps/
ADD ./trade-service-ui/target/anastasia.war /usr/local/tomcat/webapps/
ADD ./.resource_files/context.xml /usr/local/tomcat/conf/
ADD ./.resource_files/mysql-connector-j-8.3.0.jar /usr/local/tomcat/lib
EXPOSE 8080
CMD ["catalina.sh", "run"]