FROM maven:3.9.8-eclipse-temurin-22-alpine as builder

WORKDIR /app-builder

COPY . .

RUN mvn clean package

FROM eclipse-temurin:22.0.2_9-jre-alpine

RUN apk add bash

WORKDIR /app

ENV ORIGEM_PERMITIDA=frontend
ENV DATASOURCE_URL=jdbc:postgresql://db/dolar-api
ENV DATASOURCE_USERNAME=postgres
ENV DATASOURCE_PASSWORD=mysecretpassword

COPY --from=builder /app-builder/target/dolar-api-1.0.0.jar /app
COPY ./wait-for-it.sh /usr/local/bin/wait-for-it.sh

RUN chmod +x /usr/local/bin/wait-for-it.sh

ENTRYPOINT ["wait-for-it.sh", "db:5432", "--timeout=300", "--"]

CMD ["java", \
    "-Dspring-boot.run.arguments=--origem-permitida=${ORIGEM_PERMITIDA}", \
    "-Dspring.datasource.url=${DATASOURCE_URL}", \
    "-Dspring.datasource.username=${DATASOURCE_USERNAME}", \
    "-Dspring.datasource.password=${DATASOURCE_PASSWORD}", \
    "-jar", \
    "./dolar-api-1.0.0.jar"]
