# Use the official OpenJDK base image for Java 8
FROM openjdk:8-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from your target directory to the container
COPY target/PlayWrite-1.0-SNAPSHOT.jar app.jar

# Expose the port that your Spring Boot application will run on (default is 8080)
EXPOSE 8080

# Specify the command to run your Spring Boot application
CMD ["java", "-jar", "app.jar"]
