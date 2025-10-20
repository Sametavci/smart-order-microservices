# ---- Base image ----
FROM openjdk:17-slim

# ---- Set working directory ----
WORKDIR /app

# ---- Copy Maven files and build ----
COPY . .

# ---- Build JAR ----
RUN ./mvnw clean package -DskipTests

# ---- Run app ----
CMD ["java", "-jar", "target/notification-service-0.0.1-SNAPSHOT.jar"]
