# user-api

## Object
Rest controller for users management. the properties files are in Git repository :
https://github.com/pedsf1968/properties-repository.git

Openapi 3.0 documentation generated at :
/v3/api-docs

Swagger-ui server at :
/swagger-ui.html

## Profiles.
- Development : use an embeded H2 database.
- Production : query a Postgres database
- Docker : query a Postgres:alpine containerized database

## Model User
- id : Integer,
- firstName* : String,
- lastName* : String,
- email* : String,
- phone : String,
- photoLink : String,
- status : String,
- counter : Integer,
- roles : Set<Role>
- street1* : String,
- street2 : String,
- street3 : String,
- zipCode* : String,
- city* : String,
- country : String

## Model Role
- id : Integer,
- name* : String,
- users* : Set<User> 
   
## User controller
- GET /users/{userId} : get one User by ID
- GET /user/email/{email} : get one User by email
- GET /users : get all Users
- POST /users : create a User
- PUT /users/{userId} : update a User by ID
- DELETE /users/{userId} : delete a User by ID
- POST /users/searches : get all Users with filter
