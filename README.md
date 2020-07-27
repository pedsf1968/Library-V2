# library
Library project

[![Build Status](https://travis-ci.org/pedsf1968/Library-V2.svg?branch=master)](https://travis-ci.org/pedsf1968/Library-V2)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/dashboard?id=pedsf1968_Library-V2)
[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=pedsf1968_Library-V2)](https://sonarcloud.io/dashboard?id=pedsf1968_Library-V2)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=pedsf1968_Library-V2&metric=bugs)](https://sonarcloud.io/dashboard?id=pedsf1968_Library-V2)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=pedsf1968_Library-V2&metric=code_smells)](https://sonarcloud.io/dashboard?id=pedsf1968_Library-V2)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=pedsf1968_Library-V2&metric=coverage)](https://sonarcloud.io/dashboard?id=pedsf1968_Library-V2)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=pedsf1968_Library-V2&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=pedsf1968_Library-V2)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=pedsf1968_Library-V2&metric=ncloc)](https://sonarcloud.io/dashboard?id=pedsf1968_Library-V2)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=pedsf1968_Library-V2&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=pedsf1968_Library-V2)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pedsf1968_Library-V2&metric=alert_status)](https://sonarcloud.io/dashboard?id=pedsf1968_Library-V2)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=pedsf1968_Library-V2&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=pedsf1968_Library-V2)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=pedsf1968_Library-V2&metric=security_rating)](https://sonarcloud.io/dashboard?id=pedsf1968_Library-V2)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=pedsf1968_Library-V2&metric=sqale_index)](https://sonarcloud.io/dashboard?id=pedsf1968_Library-V2)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=pedsf1968_Library-V2&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=pedsf1968_Library-V2)


# How to
## Starting with Docker
- Copy docker-compose.yml.
- Copy init.sql in the same directory.
- Execute : docker-compose up -d
- Open a browser in http://localhost:9000
- Connect with the user martin.dupont@gmail.com and password martin

You can change database content in ini.sql. 
Remove the volume project-library_pgdata to start with a new database.

## Starting development mode
- clone all microservices in the same directory.
- select development profile
- launch microservices in the way list and specify the port for user-api and library-api.   


## Starting local mode
- you must have a PostgreSQL server running.
- In user-api-local.properties and library -api-local.properties set :
  - spring.datasource.initialize=true
  - spring.jpa.hibernate.ddl-auto = create
- Clone all microservices GitLab repositories in the same directory.
- Select local profile
- Launch microservices in the way list and specify the port for user-api and library-api.
  -Dserver.port=7xxx for user-api
  -Dserver.port=8xxx for library-api 
- Execute the SQL scripts data.sql located in :
   - \library-api\src\main\resources\data.sql  
   - \user-api\src\main\resources\data.sql  
- Create the directory trees for media-api images :
  - media-api.upload.tmp=/TMP/
  - media-api.images.repository=/images/library/
  - media-api.book.images.repository=/images/library/book/
  - media-api.music.images.repository=/images/library/music/
  - media-api.video.images.repository=/images/library/video/
  - media-api.game.images.repository=/images/library/game/
  - media-api.user.images.repository=/images/library/user/
- If needed you can change it in media-api-development.properties
Image name is the media.id with jpg extension and must be placed in the right directory. 

# Microservices
## config-server
Cloud configuration server that you need to start first.
### Port
Use the port 2000

## eureka-server
Server to register all microservices used by ribbon for the load balancing
### Port
Use the port 2100

## zuul-server
Router for the application.
### Port
Use the port 2200

## media-api
REST controller for images distribution to the frontend.
### Port
Use the port 4000

## mail-api
REST controller for sending mails. It contain as service that collect messages from Karafka and mailing them.
### Port
Use the port 5000
### OpenAPI3 doc 
http://localhost:5000/swagger-ui.html
  
## batch
Execute once a day a request to know delayed borrowing, build message and send them to Karafka.

## user-api
RESTful controller for user management.
### Port
Use the port 7000
### OpenAPI3 doc 
http://localhost:7000/swagger-ui.html

## library-api
RESTful controller for business management.
### Port
Use the port 8000
### OpenAPI3 doc 
http://localhost:8000/swagger-ui.html

## web-api
The frontend API for the users.
### Port
Use the port 9000

## Database library
PostgreSQL:alpine 
Use the port 5432
