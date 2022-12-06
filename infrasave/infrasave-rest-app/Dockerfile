FROM openjdk:17-alpine

COPY src ./src
COPY pom.xml ./
COPY mvnw ./
COPY .mvn/ .mvn

CMD ["./mvnw", "spring-boot:run"]