## spring-hateos-restfull-api

spring hateos restfull api richardson maturity model

## database h2

## run project
* mvn spring-boot:run

or 

* mvn package
* cd target
* java -jar restfull-api-hateos-0.0.1-SNAPSHOT.jar


## dockerise

* docker build -t img/java17 .
* docker run -it -p 8080:8080 -v ./app:/app -v /app/target --name container_java17 img/java17
