# Copy the Maven POM file and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the Spring Boot application
COPY src src
RUN mvn package -DskipTests

# Use the official OpenJDK image for Java 17 as the runtime stage
FROM adoptopenjdk:17-jre-hotspot
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/PlayWrite-1.0-SNAPSHOT.jar app.jar

# Expose port 443
EXPOSE 443

# Specify the command to run your Spring Boot application
CMD ["java", "-jar", "app.jar"]
