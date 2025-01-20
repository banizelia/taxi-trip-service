FROM amazoncorretto:21.0.5
WORKDIR /app
COPY target/taxi-trip-service-1.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]