# ==============================================================================
# Stage 1: Build the application using Maven and Eclipse Temurin JDK 25
# ==============================================================================
FROM maven:3.9-eclipse-temurin-25 AS builder

WORKDIR /app

# Copy the build configuration file first to leverage Docker layer caching
COPY pom.xml .

# Copy the source code
COPY src ./src

# Compile and package the application, skipping unit tests for speed
RUN mvn clean package -DskipTests

# ==============================================================================
# Stage 2: Create the lightweight runtime image with Eclipse Temurin JRE 25
# ==============================================================================
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Copy the compiled JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port the application listens on
EXPOSE 8080

# Run the Java application
ENTRYPOINT ["java", "-jar", "app.jar"]
