# Use a base image with JDK 17 and Maven installed
FROM maven:3.8.4-openjdk-17-slim AS build

ARG SONAR_TOKEN
ENV SONAR_TOKEN=${SONAR_TOKEN}

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

RUN mvn -B verify \
      org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
      -Dsonar.projectKey=thiago-abarros_email \
      -Dsonar.host.url=https://sonarcloud.io \
      -Dsonar.login=${SONAR_TOKEN}

# Use a smaller base image for runtime
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar ./app.jar

# Expose the port the application runs on
EXPOSE ${SERVER_PORT_EMAIL}

# Command to run the application
CMD ["java", "-jar", "app.jar"]
