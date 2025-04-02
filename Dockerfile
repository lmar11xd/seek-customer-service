# Usa una imagen oficial de OpenJDK como base
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/seek-customer-service-1.0.0.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]
