FROM eclipse-temurin:21-jre

WORKDIR /

COPY /target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]