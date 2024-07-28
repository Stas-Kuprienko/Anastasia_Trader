FROM tomcat:11.0.0-M20-jdk21
ADD ./Trade_service/target/anastasia.war /usr/local/tomcat/webapps/
ADD ./App_UI/target/anastasia_trader.war /usr/local/tomcat/webapps/
ADD .resource_files/context.xml /usr/local/tomcat/conf/
ADD .resource_files/mysql-connector-j-8.3.0.jar /usr/local/tomcat/lib
EXPOSE 8080
CMD ["catalina.sh", "run"]