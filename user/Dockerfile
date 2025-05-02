# Use a base image with JDK 17 and Maven installed
FROM maven:3.8.4-openjdk-17-slim AS build

ARG MODULE_NAME

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the application, skipping unit tests
RUN mvn clean package -DskipTests

# Run SonarQube analysis, skipping integration tests
RUN mvn -B verify -DskipITs=true \
      org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
      -Dsonar.projectKey=thiago-abarros_microservices-rabbitmq_${MODULE_NAME} \
      -Dsonar.host.url=https://sonarcloud.io \
      -Dsonar.login=${SONAR_TOKEN}

# Use a smaller base image for runtime
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar ./app.jar

# Expose the port based on MODULE_NAME
EXPOSE ${MODULE_NAME}_PORT

# Command to run the application
CMD ["java", "-jar", "app.jar"]