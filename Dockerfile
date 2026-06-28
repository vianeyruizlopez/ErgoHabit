FROM eclipse-temurin:21-jre-alpine
EXPOSE 8080
RUN mkdir /app

# Copia el archivo FatJar que genera el plugin oficial de Ktor
COPY build/libs/*.jar /app/ergo-habit.jar

ENTRYPOINT ["java", "-jar", "/app/ergo-habit.jar"]