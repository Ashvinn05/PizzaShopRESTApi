# Use Maven as the base image for building
FROM maven:3.9.5-eclipse-temurin-17 as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY src/main/resources/application.properties ./src/main/resources/
RUN mvn clean package -DskipTests

# Use a slim Java runtime image for production
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]