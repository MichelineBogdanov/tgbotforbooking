#FROM maven:3.6.0-jdk-11-slim AS build
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
VOLUME /tmp
WORKDIR /app
COPY credentials.json /app/credentials.json
COPY /target/tgbotforbooking.jar /app/tgbotforbooking.jar
ENTRYPOINT ["java","-jar","/app/tgbotforbooking.jar"]