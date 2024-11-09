# Use a multi-stage build to reduce image size
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /netflix-clone
COPY . .
RUN mvn clean install

FROM eclipse-temurin:17-jdk
WORKDIR /netflix-clone
COPY --from=build /netflix-clone/target/netflix-clone-0.0.1-SNAPSHOT.jar netflix-clone-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "netflix-clone-0.0.1-SNAPSHOT.jar"]