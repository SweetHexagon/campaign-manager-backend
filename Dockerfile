FROM openjdk:17-jdk-slim

WORKDIR /app

COPY out/artifacts/campaign_manager_jar/campaign-manager.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
