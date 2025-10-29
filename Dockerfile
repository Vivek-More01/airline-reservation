# Dockerfile for Spring Boot Application

# ---- Build Stage ----
# Use an official Maven image containing JDK 21 (match your project's Java version if different)
# Using '-eclipse-temurin' for a widely adopted distribution
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project definition first to leverage Docker layer caching
COPY pom.xml ./
# Download dependencies (only runs if pom.xml changes)
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the application JAR file, skipping tests
# Use the 'spring-boot:repackage' goal to create an executable JAR
RUN mvn package -DskipTests spring-boot:repackage

# ---- Run Stage ----
# Use an official lightweight JRE image (match the JDK version from builder stage)
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the executable JAR file created in the builder stage
# Adjust the JAR filename pattern if your pom.xml produces a different name
COPY --from=builder /app/target/airline-reservation-springboot-*.jar app.jar

# Expose the port the application runs on (Spring Boot default is 8080)
EXPOSE 8080

# Set the command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
