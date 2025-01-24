FROM maven:3.9.5-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/dragons-*.jar app.jar
EXPOSE 8080

COPY ./data/dragons.mv.db /data/dragons.mv.db
VOLUME /data

ENTRYPOINT ["java", "-jar", "app.jar"]
