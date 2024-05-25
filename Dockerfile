FROM amazoncorretto:17
COPY /target/RealEstate-0.0.1-SNAPSHOT.jar ./RealEstate-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "RealEstate-0.0.1-SNAPSHOT.jar"]
