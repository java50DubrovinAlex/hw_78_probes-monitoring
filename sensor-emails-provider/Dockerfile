FROM eclipse-temurin:21-jdk-alpine
COPY ./target/sensor-emails-provider-0.0.1.jar app.jar
ENV CONFIG_SERVER_HOST=config-server
ENTRYPOINT [ "java", "-jar", "app.jar" ]