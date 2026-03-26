
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY pom.xml .
LABEL "org.opencontainers.image.authors"="ArthurKH01"
COPY target/test_task-0.0.1-SNAPSHOT.jar test_task-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "test_task-0.0.1-SNAPSHOT.jar"]