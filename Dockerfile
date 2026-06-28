FROM gradle:8-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle buildFatJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
EXPOSE 8080
RUN mkdir /app
COPY --from=build /app/build/libs/ktor-api-all.jar /app/ergo-habit.jar
ENTRYPOINT ["java", "-jar", "/app/ergo-habit.jar"]
