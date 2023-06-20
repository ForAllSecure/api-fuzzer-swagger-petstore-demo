FROM openjdk:8-jre-alpine

WORKDIR /swagger-petstore

COPY target/lib/jetty-runner.jar /swagger-petstore/jetty-runner.jar
COPY target/*.war /swagger-petstore/server.war
COPY src/main/resources/openapi.yaml /swagger-petstore/openapi.yaml
COPY inflector.yaml /swagger-petstore/

RUN apk update && apk add --no-cache curl

# Download JaCoCo agent (0.8.6 for Java 8)
RUN curl -o /jacocoagent.jar https://repo1.maven.org/maven2/org/jacoco/org.jacoco.agent/0.8.6/org.jacoco.agent-0.8.6-runtime.jar

EXPOSE 8080
EXPOSE 6300

CMD ["java", "-javaagent:/jacocoagent.jar=address=*,port=6300,output=tcpserver", "-jar", "-DswaggerUrl=openapi.yaml", "/swagger-petstore/jetty-runner.jar", "--log", "/var/log/yyyy_mm_dd-requests.log", "/swagger-petstore/server.war"]
