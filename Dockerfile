# syntax=docker/dockerfile:1
FROM openjdk:18-alpine
COPY target/redis-springdoc.jar  redis-springdoc.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/redis-springdoc.jar"]

