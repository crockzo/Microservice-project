# ======================
# Stage 1: Build stage
# ======================
FROM gradle:jdk17-corretto AS builder

WORKDIR /app

# Copy Gradle files first for dependency caching
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Download dependencies (caches layers)
RUN ./gradlew dependencies --no-daemon || return 0

# Copy source code
COPY src ./src

# Build application (skip tests for speed inside Docker)
RUN ./gradlew clean bootJar --no-daemon -x test

# ======================
# Stage 2: Runtime stage
# ======================
FROM arm64v8/eclipse-temurin:17-jre


# Add non-root user
# RUN apk add --no-cache shadow \
#  && groupadd -r spring \
#  && useradd -r -g spring spring

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose default Spring Boot port
EXPOSE 8761

# Allow runtime JVM args via JAVA_OPTS
# Example: docker run -e JAVA_OPTS="-Xms512m -Xmx1g" ...
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
