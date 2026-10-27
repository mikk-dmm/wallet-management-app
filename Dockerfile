# Build Stage
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /build

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .


RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package -DskipTests


# Runtime Stage
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

RUN useradd -m appuser && chown -R appuser:appuser /app

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]
