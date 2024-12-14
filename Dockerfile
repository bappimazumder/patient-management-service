# Use an official OpenJDK runtime as a parent image for the build stage
FROM openjdk:17-alpine as build

# Set the working directory
WORKDIR /patient-management

# Copy the build files
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle ./gradle
COPY src ./src

# Make the Gradle wrapper executable
RUN chmod +x gradlew

# Build the application (WAR file)
RUN ./gradlew clean build -x test

# Second stage: create a minimal image with just the WAR file
FROM openjdk:17-alpine

# Set the working directory
WORKDIR /patient-management

# Copy the WAR file from the build stage
COPY --from=build /patient-management/build/libs/*.war /patient-management/patient-management.jar

# Expose the port the app runs on (adjust if needed)
EXPOSE 8080

# Run the application using the Spring Boot loader
ENTRYPOINT ["java", "-jar", "patient-management.jar"]