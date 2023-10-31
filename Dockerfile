# Use the official maven/Java 11 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM maven:3-openjdk-18-slim AS build-env
#FROM maven:3-j

# Set the working directory to /app
WORKDIR /app
# Copy the pom.xml file to download dependencies
COPY pom.xml ./
# Copy local code to the container image.
COPY src ./src

# Download dependencies and build a release artifact.
RUN mvn package -DskipTests

# Use OpenJDK for base image.
# https://hub.docker.com/_/openjdk
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
#FROM openjdk:11.0.16-jre-slim
FROM openjdk:18-ea-1-jdk-slim
# Copy the jar to the production image from the builder stage.
COPY --from=build-env /app/target/play-write-*.war /play-write.war

# Run the web service on container startup.
CMD ["java", "-jar", "/play-write.war"]
