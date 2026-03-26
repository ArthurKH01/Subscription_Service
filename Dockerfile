
FROM eclipse-temurin:21-jdk

LABEL "org.opencontainers.image.authors"="ArthurKH01"


COPY target/test_task-0.0.1-SNAPSHOT.jar test_task-0.0.1-SNAPSHOT.jar

# execute the application
ENTRYPOINT ["java", "-jar", "test_task-0.0.1-SNAPSHOT.jar"]