# Stage 1: Build the jar using Maven
FROM maven:3.8.7-openjdk-8 AS build

WORKDIR /app

# Copy pom and source code
COPY pom.xml .
COPY src ./src

# Build jar (skip tests to speed up)
RUN mvn clean package

# Stage 2: Run the application using a lightweight JRE
FROM openjdk:8-jdk-alpine

WORKDIR /app

# Copy jar from the build stage
COPY --from=build /app/target/management-service.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
