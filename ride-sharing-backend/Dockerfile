# Use an official Maven image to build the project
FROM maven:3.9.2-eclipse-temurin-17 AS builder

# Set working directory inside the image
WORKDIR /app

# Copy pom.xml and download dependencies separately (to leverage Docker cache)
COPY pom.xml .

RUN mvn -B dependency:go-offline

# Copy the source code
COPY src ./src

# Package the application
RUN mvn -B clean package -DskipTests

# Use a lightweight Java runtime image for running the app
FROM eclipse-temurin:17-jre-alpine

# Set working directory for the runtime container
WORKDIR /app

# Copy the jar from the builder stage
COPY --from=builder /app/target/ride-sharing-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-jar","app.jar"]
