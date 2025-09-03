# Use OpenJDK 21 as base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Create a new stage for the runtime
FROM openjdk:21-jre-slim

# Set working directory
WORKDIR /app

# Create a non-root user
RUN groupadd -r bookkeeping && useradd -r -g bookkeeping bookkeeping

# Copy the built JAR file
COPY --from=0 /app/target/bookkeeping-backend-*.jar app.jar

# Change ownership to non-root user
RUN chown bookkeeping:bookkeeping app.jar

# Switch to non-root user
USER bookkeeping

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]