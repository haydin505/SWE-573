# Infrasave

This project developed for SWE-573 lecture.

You can access demo of the application with following link:
https://dialecticsoftware.com/

## How to build this project?

You can use make or manual commands to build application.
make command can be installed via ``brew install make``
* Docker and docker-compose must be installed on your machine.
### 1. Build Frontend 
Run
```
make build-frontend
``` 
or
```
infrasave/infrasave-react-client/node/npm ci --prefix infrasave/infrasave-react-client && infrasave/infrasave-react-client/node/npm start --prefix infrasave/infrasave-react-client
```
### 2. Launch Database and Create Schema
Run 
```
make build-db
``` 
or 
```
docker-compose -f infrasave/docker-compose-local.yml up -d db
```
### 2. Build Backend
Run 
```
make build-backend
```
or
```
cd infrasave/infrasave-rest-app && ./mvnw spring-boot:run
```

Now you can access application with the following link:
* http://localhost:3000

---

## Prod environment:
* Run following command `docker-compose -f infrasave/docker-compose.yml up -d`

Now you can access application with the following link: http://localhost:80


