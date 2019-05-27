FROM openjdk:8-alpine

COPY ./target/base64-diff-api-1.0.0-RELEASE.jar /app/base64-diff-api-1.0.0-RELEASE.jar
WORKDIR /app

CMD ["java", "-jar", "base64-diff-api-1.0.0-RELEASE.jar", "--spring.profiles.active=${APP_PROFILE}", "-Xms=400M", "-Xmx=400M"  ]