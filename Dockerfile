FROM openjdk:23-slim-bullseye
COPY target/connectify-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080