FROM openjdk:17-jdk-alpine
MAINTAINER baeldung.com
COPY  target/HealthArc-0.0.1-SNAPSHOT.jar  healtharc-server-0.0.1.jar
ENTRYPOINT ["java","-jar","/healtharc-server-0.0.1.jar"]