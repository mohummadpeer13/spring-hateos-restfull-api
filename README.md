## spring-hateos-restfull-api

spring hateos restfull api richardson maturity model

## database h2

## run project
* mvn spring-boot:run

or 

* mvn package
* cd target
* java -jar restfull-api-hateos-0.0.1-SNAPSHOT.jar


## dockerise app

* docker build -t img/java17 .
* docker run -it -p 80:8080 -v ./app:/app -v /app/target --name cont_jdk17_rest_full_api img/java17

## routes
* GET http://localhost/api/accounts

* GET http://localhost/api/accounts/1

* POST http://localhost/api/accounts
  content-type: application/json

  {
    "accountNumber": "4152637890",
    "balance": 1400
  }
  
* PUT  http://localhost/api/accounts
  content-type: application/json

  {
    "id" : 1,
    "accountNumber" : "1234567893535",
    "balance" : "12"
  }
  
* DELETE   http://localhost/api/accounts/2

* PATCH   http://localhost/api/accounts/1/deposit
  content-type: application/json

  {
    "amount" : "500"
  }
  
* PATCH   http://localhost/api/accounts/1/withdraw
  content-type: application/json

  {
    "amount" : "100"
  }
