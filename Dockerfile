FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

RUN apt-get update && apt-get install -y git maven

RUN git clone https://github.com/Zepbl4/test_stub .

RUN mvn clean package 

EXPOSE 8080 8778

CMD ["java", "-javaagent:/app/jolokia-agent-jvm-2.2.6-javaagent.jar=port=8778,host=0.0.0.0", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
