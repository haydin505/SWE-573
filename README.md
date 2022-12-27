# Infrasave

This project developed for SWE-573 lecture.

You can access demo of the application with following link:
https://dialecticsoftware.com/

## Table of Contents

- [Set Environment Variables](#set-environment-variables)
- [How to Build Project for Development?](#how-to-build-this-project-for-development)
- [Local Dev Environment With Docker](#local-dev-environment-with-docker)
- [Prod Environment](#prod-environment)

<a name="set-environment-variables"></a>

## Set Essential Environment Variables

Mail credentials must be specified in operating system environment variables.

You can give your custom mail or get
production environment mail credentials from owner of the repository.

Open your terminal end paste below code or paste it to `~/.bashrc`

```
export mail_username=
export mail_password=
```

<a name="how-to-build-this-project-for-development"></a>

## How to Build Project for Development?

You can use make or manual commands to build the application.`make` command can be installed via ``brew install make``

### Requirements

* Docker and docker-compose
* Java 17

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

### 3. Build Backend

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

<a name="local-dev-environment-with-docker"></a>

## Local Dev Environment With Docker

You can use this type of build process in order to test or examine the application.

* Run following command

```
docker-compose -f infrasave/docker-compose-local.yml build && \
docker-compose -f infrasave/docker-compose-local.yml create && \
docker-compose -f infrasave/docker-compose-local.yml start
```

Now you can access application with the following link: http://localhost:3000

<a name="prod-environment"></a>

## Prod Environment

* Remove local images with following command

```
docker-compose -f infrasave/docker-compose.yml down --remove-orphans --rmi all --volumes
```

* You must set environment variables in your operations system for following properties

- Please contact owner of the repository in order to get value of the below environment variables.

```
export REACT_APP_BASE_URL=https://dialecticsoftware.com/api
export active_profile=
export datasource_url=
export datasource_username=
export datasource_password=
export mail_username=
export mail_password=
 ``` 

* Run following command

```
 docker-compose -f infrasave/docker-compose.yml up -d
```

Now you can access application with the following link: http://localhost:3000


