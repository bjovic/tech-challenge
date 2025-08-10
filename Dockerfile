# Multi-stage build: Build stage
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src/ src/

# Make gradlew executable and build the application
RUN chmod +x ./gradlew && \
    ./gradlew clean bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Create a non-root user for security
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Copy the JAR file from build stage
COPY --from=builder /app/build/libs/techchallenge-0.0.1-SNAPSHOT.jar app.jar

# Change ownership of the app directory
RUN chown -R spring:spring /app

USER spring:spring

# Expose the port your Spring Boot app runs on (default is 8080)
EXPOSE 8080

# Set JVM options (optional but recommended)
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check (optional)
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the JAR file
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]