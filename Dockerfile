FROM maven:3-eclipse-temurin-21 AS build
WORKDIR /app
RUN git clone https://github.com/Zepbl4/test_stub .
RUN mvn clean package 

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar /app/test_stub.jar
COPY --from=build /app/jolokia-agent-jvm-2.2.6-javaagent.jar /app/jolokia.jar
EXPOSE 8080 8778
CMD ["java", "-javaagent:/app/jolokia.jar=port=8778,host=0.0.0.0", "-jar", "/app/test_stub.jar"]
