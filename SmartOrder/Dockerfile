FROM openjdk:17-slim AS builder

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src
COPY libs ./libs

RUN chmod +x mvnw \
    && ./mvnw install:install-file \
        -Dfile=libs/inventory-service-0.0.1-SNAPSHOT.jar \
        -DgroupId=com.smartorder \
        -DartifactId=inventory-service \
        -Dversion=0.0.1-SNAPSHOT \
        -Dpackaging=jar \
    && ./mvnw clean package -DskipTests

FROM openjdk:17-slim
WORKDIR /app

COPY --from=builder /app/target/SmartOrder-0.0.1-SNAPSHOT.jar ./smartorder.jar

CMD ["java", "-jar", "smartorder.jar"]
