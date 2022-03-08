FROM maven:3.6.0-jdk-11 AS MAVEN_BUILD
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package

FROM openjdk:11-jre-slim-stretch
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/ledcontroller-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "ledcontroller-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080