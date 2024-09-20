FROM openjdk:17-jdk-slim

WORKDIR /app

# Setting up the mail service
RUN apt-get update && apt-get install -y git
RUN git clone https://github.com/mindzone-api/mz-mail-service.git
WORKDIR /app/mz-mail-service
RUN chmod +x ./mvnw
RUN ./mvnw clean package

# Setting up the backend itself
WORKDIR /app
COPY . /app/mz-backend
WORKDIR /app/mz-backend
RUN chmod +x ./mvnw
RUN ./mvnw clean package

EXPOSE 8080 8081
CMD java -jar /app/mz-mail-service/target/*.jar & java -jar /app/mz-backend/target/*.jar
