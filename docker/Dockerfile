FROM openjdk:17-jdk-slim

WORKDIR /app

# Setting up the mail service
RUN apt-get update && apt-get install -y git
RUN git clone https://github.com/mindzone-api/mz-mail-service.git
WORKDIR /app/mz-mail-service
RUN chmod +x ./mvnw
RUN ./mvnw clean package

# Setting up the backend itself
ARG CHAT_MESSAGE_ENCRYPTION_KEY
ARG GOOGLE_CLIENT_ID
ARG GOOGLE_CLIENT_SECRET
ARG MONGODB_URI
ARG RABBITMQ_URI
ARG REDIS_HOST
ARG MZ_MAIL_USERNAME
ARG MZ_MAIL_PASSWORD

ENV CHAT_MESSAGE_ENCRYPTION_KEY=$CHAT_MESSAGE_ENCRYPTION_KEY
ENV GOOGLE_CLIENT_ID=$GOOGLE_CLIENT_ID
ENV GOOGLE_CLIENT_SECRET=$GOOGLE_CLIENT_SECRET
ENV MONGODB_URI=$MONGODB_URI
ENV RABBITMQ_URI=$RABBITMQ_URI
ENV REDIS_HOST=$REDIS_HOST
ENV MZ_MAIL_USERNAME=$MZ_MAIL_USERNAME
ENV MZ_MAIL_PASSWORD=$MZ_MAIL_PASSWORD

WORKDIR /app
COPY .. /app/mz-backend
WORKDIR /app/mz-backend
RUN chmod +x ./mvnw
RUN ./mvnw clean package

EXPOSE 8080 8081
CMD java -jar /app/mz-mail-service/target/*.jar & java -jar /app/mz-backend/target/*.jar
