FROM ubuntu:latest
WORKDIR /app
COPY . .
RUN apt update -y
RUN apt install -y nano
RUN apt install -y openjdk-17-jdk
RUN apt install -y openjdk-17-jre
RUN apt install -y maven
CMD ["mvn","spring-boot:run"]
