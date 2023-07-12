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
* docker run -it -p 80:8080 -v ./app:/app -v /app/target --name cont_jdk17_rest_full_api img/java17
* on browser http://localhost/api/accounts
