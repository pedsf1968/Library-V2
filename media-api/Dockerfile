#
# media-api : Dockerfile
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
COPY src/main/resources/images /images/
COPY --from=build /srv/target/media-api.jar /srv/media-api.jar
WORKDIR /srv
RUN sh -c 'touch media-api.jar'
EXPOSE 4000

ENTRYPOINT ["java","-jar","media-api.jar"]
