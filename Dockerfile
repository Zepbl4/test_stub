FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

RUN apt-get update && apt-get install -y git maven

RUN git clone https://github.com/Zepbl4/test_stub .

RUN mvn clean package 

RUN curl -o /app/jolokia-jvm-2.2.6-javaagent.jar https://search.maven.org/remotecontent?filepath=org/jolokia/jolokia-agent-jvm/2.2.6/jolokia-agent-jvm-2.2.6-javaagent.jar

EXPOSE 8080 8778

CMD ["java", "-javaagent:/app/jolokia-agent-jvm-2.2.6-javaagent.jar=port=8778,host=0.0.0.0", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
