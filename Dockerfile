FROM openjdk:8-jdk-alpine

RUN mkdir -p /opt/m3-widget-datetime/

ARG JAR_FILE
COPY ${JAR_FILE} /opt/m3-widget-datetime/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/m3-widget-datetime/app.jar"]