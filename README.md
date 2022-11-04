# Infrasave

This project developed for SWE-573 lecture

## How to build this project?
* Switch directory to infrasave with following command `cd infrasave/`
* Please run `mvn clean install` in order to build project.
* Start MySql instance with docker-compose `docker-compose -f  infrasave-rest-app/docker-compose.yml up`
* Run `java -jar infrasave-rest-app/target/infrasave-rest-app-0.0.1-SNAPSHOT-jar-with-dependencies.jar`
* Run `serve -p 80 infrasave-react-cliend/build/`

Now you can access application at http://localhost:80
