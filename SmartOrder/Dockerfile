# 1️⃣ Maven ile build (builder aşaması)
FROM openjdk:17-slim AS builder

WORKDIR /app

# Maven dosyalarını kopyala
COPY SmartOrder/mvnw .
COPY SmartOrder/.mvn .mvn
COPY SmartOrder/pom.xml .
COPY SmartOrder/src ./src

# local dependency jar'ı kopyala
COPY SmartOrder/libs /app/libs

# inventory-service'i Maven local repo'ya yükle ve build et
RUN chmod +x mvnw \
    && ./mvnw install:install-file \
        -Dfile=libs/inventory-service-0.0.1-SNAPSHOT.jar \
        -DgroupId=com.smartorder \
        -DartifactId=inventory-service \
        -Dversion=0.0.1-SNAPSHOT \
        -Dpackaging=jar \
    && ./mvnw clean package -DskipTests

# 2️⃣ Runtime image (sadece jar'ı çalıştırır)
FROM openjdk:17-slim
WORKDIR /app

# Builder aşamasından çıktı jar'ı kopyala
COPY --from=builder /app/target/SmartOrder-0.0.1-SNAPSHOT.jar ./smartorder.jar

# Çalıştırma komutu
CMD ["java", "-jar", "smartorder.jar"]
