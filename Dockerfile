FROM eclipse-temurin:21-jre-alpine
EXPOSE 8080
RUN mkdir /app

COPY build/libs/ktor-api-all.jar /app/ergo-habit.jar

ENTRYPOINT ["java", "-jar", "/app/ergo-habit.jar"]