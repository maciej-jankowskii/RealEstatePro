FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY RealEstate/pom.xml .
COPY RealEstate/src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/RealEstate-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]