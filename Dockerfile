FROM maven:3.9.6-eclipse-temurin-22-alpine AS builder

COPY ./pom.xml ./pom.xml
COPY ./common/pom.xml ./common/pom.xml
COPY ./common/bot/pom.xml ./common/bot/pom.xml
COPY ./common/dao/pom.xml ./common/dao/pom.xml
COPY ./common/localization/pom.xml ./common/localization/pom.xml
COPY ./common/transport-client/pom.xml ./common/transport-client/pom.xml
COPY ./common/transport-client-impl/pom.xml ./common/transport-client-impl/pom.xml
COPY ./tg-assistant/pom.xml ./tg-assistant/pom.xml
RUN mvn dependency:go-offline

COPY ./common ./common
COPY ./tg-assistant ./tg-assistant
RUN mvn clean package

FROM eclipse-temurin:22-jre-alpine
COPY --from=builder ./tg-assistant/target/tg-assistant-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar
COPY ./json-settings ./json-settings
ENTRYPOINT ["java", "-jar", "./app.jar"]
