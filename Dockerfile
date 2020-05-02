FROM openjdk:11-jre-slim

RUN groupadd -r demogroup && useradd -r -g demogroup demouser

WORKDIR /app

USER demouser

COPY build/libs/batch-demo.jar batch-demo.jar

COPY docker .

ENTRYPOINT /app/start-app.sh