FROM openjdk:17-alpine

COPY src ./src
COPY pom.xml ./
COPY mvnw ./
COPY .mvn/ .mvn
RUN ./mvnw clean install -DskipTests
EXPOSE 8080

CMD ["java", "-jar", "./target/infrasave-rest-app.jar"]