# Use the official Maven image for building your Spring Boot application
FROM maven:3.8.4-openjdk-17 AS build
# Set the working directory in the container
WORKDIR /app

# Copy the Maven project definition and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code into the container
COPY src ./src

# Build the Spring Boot application
RUN mvn package -DskipTests

# Create a dedicated image for running the application
FROM openjdk:8-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file built in the previous stage into the container
COPY --from=build /app/target/PlayWrite-1.0-SNAPSHOT.jar app.jar

# Expose the port that your Spring Boot application will run on (default is 8080)
EXPOSE 8080

# Specify the command to run your Spring Boot application
CMD ["java", "-jar", "app.jar"]
