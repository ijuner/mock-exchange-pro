# Use official Maven image to build the application
FROM maven:3.9.2-eclipse-temurin-17 as build
WORKDIR /app

# Copy source code
COPY . .

# Package the application (skip tests for speed)
RUN mvn clean package -DskipTests

# Use JDK 17 image to run the app
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
