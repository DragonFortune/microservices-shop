# Билд-стадия: собираем JAR внутри контейнера
FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /app

# Копируем gradle скрипты и зависимости
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Кэшируем зависимости
RUN ./gradlew build --no-daemon || return 0

# Копируем весь код
COPY . .

# Собираем jar
RUN ./gradlew clean build -x test --no-daemon

# Рантайм-стадия
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Берём JAR из билд-стадии
COPY --from=builder /app/build/libs/order-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
