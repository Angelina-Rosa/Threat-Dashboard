# ---- Build Stage ----
# Use Maven + Java 17 to compile and package the app
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first — Docker caches this layer so dependencies
# only re-download when pom.xml changes, not on every code change
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the jar
COPY src ./src
RUN mvn clean package -DskipTests


# ---- Run Stage ----
# Use a smaller JRE-only image for the final container (no Maven needed)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy only the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
