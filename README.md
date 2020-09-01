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
## Starting in development mode with in memory database
Clone all microservices project.
Select development profile
Launch microservices in following order starting with servers :
- config-server
- eureka-server
- zuul-server

Continue with backend REST api
- media-api
- user-api
- library-api
- mail-api

Finish with frontend web user interface and batch
- web-api
- batch

Use following credentials below to play with the application
- emile.zola@free.fr /zola
- victor.hugo@gmail.com / hugo
- martin.dupont@gmail.com / martin

## Starting local mode with PostgreSQL database
### create library database
Database is configured with :
- host : localhost
- port : 5433
- database : library

You can change the spring.datasource.url property in files :
- config-server/properties-repository/library-api-local.properties
- config-server/properties-repository/user-api-local.properties

Execute the SQL script below to create database with datas
- db-init-from-scratch.sql

Launch microservices like previous section with profile local.

## Starting with Docker
- Copy docker-compose.yml.
- Copy init.sql in the same directory.
- Execute : docker-compose up -d
- Open a browser in http://localhost:9000
- Connect with the user martin.dupont@gmail.com and password martin

You can change database content in ini.sql. 
Remove the volume project-library_pgdata to start with a new database.


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
Use the port 5433
