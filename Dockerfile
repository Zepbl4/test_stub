FROM maven:3-eclipse-temurin-21 AS build
WORKDIR /app
RUN git clone https://github.com/Zepbl4/test_stub .
RUN mvn clean package 

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/db-db-SNAPSHOT.jar /app/test_stub.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/test_stub.jar"]
