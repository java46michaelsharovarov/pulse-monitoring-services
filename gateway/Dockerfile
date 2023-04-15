FROM openjdk:17
EXPOSE 8000
ADD ./target/gateway-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]