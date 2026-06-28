#Compilar la aplicación usando Gradle con JDK 17
FROM gradle:7.6-jdk17 AS build
COPY --chown=gradle:gradle . /home/src
WORKDIR /home/src
RUN ./gradlew build -x test --no-daemon

#Ejecutar el archivo JAR generado en un entorno ligero
FROM openjdk:17-slim
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/src/build/libs/*-all.jar /app/ergo-habit.jar
ENTRYPOINT ["java", "-jar", "/app/ergo-habit.jar"]