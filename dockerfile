FROM maven:3.8.4-eclipse-temurin-17 as builder
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
WORKDIR /app
COPY credentials.json /app/credentials.json
COPY --from=builder /app/target/tgbotforbooking.jar /app/tgbotforbooking.jar
ENTRYPOINT ["java","-jar","/app/tgbotforbooking.jar"]