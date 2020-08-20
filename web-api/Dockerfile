#
# library-api : Dockerfile
#
# Build stage
#
FROM maven:3.5.2-jdk-8-alpine AS build
COPY src /srv/src
COPY pom.xml /srv
RUN mvn -f /srv/pom.xml clean package -DskipTests=true -Pdocker

#
# Package stage
#
FROM openjdk:8-jdk-alpine

COPY --from=build /srv/target/web-api.jar /srv/web-api.jar
WORKDIR /srv
RUN sh -c 'touch web-api.jar'
EXPOSE 9000

ENTRYPOINT ["java","-jar","web-api.jar"]
