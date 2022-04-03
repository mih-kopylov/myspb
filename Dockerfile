FROM openjdk:11.0-oracle

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]