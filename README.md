# Store Locator API

This is the Store Locator Application used to save and get details of store using a json file.
The microservices rest api application developed using spring boot.

## Functionalities

* API that create a or multiple store(s) with store details 
        URL := http://localhost:9900/storelocator/stores
        Method  := POST
        Sample Request  :=
                { "stores" :[
  {
    "STORE_ID": 163,
    "STORE_NAME": "Store XYZ",
    "PHONE": "+123466789",
    "ADDRESS1": "Shopping Centre, Unit 512",
    "CITY": "Amsterdam",
    "COUNTRY": "NL",
    "ZIPCODE": "1155 EG",
    "STLOCATTR": {
      "LANGUAGE_ID": 44,
      "OPENINGHOURS": "MON 09:00-21:00~~TUE 09:00-21:00~~WED 09:00-21:00~~THU 09:00-21:00~~FRI 09:00-21:00~~SAT 09:00-21:00~~SUN 09:00-20:00"
    },
    "LOCATION": {
      "LAT": "12.35973",
      "LNG": "14.87937"
    },
    "COUNTRYCODE": "NL"
  },
  {
    "STORE_ID": 113,
    "STORE_NAME": "Store ABC",
    "PHONE": "+123456789",
    "ADDRESS1": "Shopping Centre, Unit 425",
    "CITY": "Zaandam",
    "COUNTRY": "NL",
    "ZIPCODE": "5545 EG",
    "STLOCATTR": {
      "LANGUAGE_ID": 44,
      "OPENINGHOURS": "MON 09:00-21:00~~TUE 09:00-21:00~~WED 09:00-21:00~~THU 09:00-21:00~~FRI 09:00-21:00~~SAT CLOSED~~SUN CLOSED"
    },
    "LOCATION": {
      "LAT": "52.35974",
      "LNG": "40.87914"
    },
    "COUNTRYCODE": "NL"
  }]}
  
* API that can update an existing store 
      URL := http://localhost:9900/storelocator/store/<storeId>
      Method  := PUT
      Sample Request :=
                  {
    "STORE_ID": 163,
    "STORE_NAME": "Store XYZ",
    "PHONE": "+123466789",
    "ADDRESS1": "Shopping Centre, Unit 512",
    "CITY": "Amsterdam",
    "COUNTRY": "NL",
    "ZIPCODE": "1155 EG",
    "STLOCATTR": {
      "LANGUAGE_ID": 44,
      "OPENINGHOURS": "MON 09:00-21:00~~TUE 09:00-21:00~~WED 09:00-21:00~~THU 09:00-21:00~~FRI 09:00-21:00~~SAT 09:00-21:00~~SUN 09:00-20:00"
    },
    "LOCATION": {
      "LAT": "12.35973",
      "LNG": "14.87937"
    },
    "COUNTRYCODE": "NL"
  }
  
* API that can delete a store. 
      URL := http://localhost:9900/storelocator/store/<storeId>
      Method  := DELETE
  
* API that can list all stores 
      URL := http://localhost:9900/storelocator/stores
      Method  := GET
      
* API that can list all stores with following filters
      o	in a location (city) - 
                              URL := http://localhost:9900/storelocator/stores
                              Method  := GET
                              Request Param := city : <value>  
      o	in a country -
                              URL := http://localhost:9900/storelocator/stores
                              Method  := GET
                              Request Param := country : <value>
  
      o	stores within a given radius with uom (miles or kms) -
                              URL := http://localhost:9900/storelocator/stores
                              Method  := GET
                              Request Param := radius : <value>
                                               location : <value>  OR     latitude : <value>,longitude : <value>
                                               uom : kms or miles (optional)
                              
      o	stores that are open at the current time and date -
                              URL := http://localhost:9900/storelocator/stores
                              Method  := GET
                              Request Param := current : true
                                               



## Requirements

* [Java](https://www.oracle.com/java/)
* [Maven](https://maven.apache.org/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [jUnit](https://junit.org/)
* [Docker](https://www.docker.com/)

## Installation

Use the maven command line interface [Maven](https://maven.apache.org/) to install the dependencies

```bash
mvn install -DSkipTests
```
## Test
Use the maven command line interface [Maven](https://maven.apache.org/) to perform unit test
```bash
mvn test
```
## Docker build
```bash
docker build --rm -f "Dockerfile" -t store-locator:latest "."
```

### How the docker image is build
* Base image for the docker container is `adoptopenjdk/openjdk11-openj9:alpine-slim`
* The dependency libraries under `/target/dependency/lib` are copied to the `/app/lib` in the image
* Resources files (for e.g. configuration files , properties files etc.) under `/target/dependency/resources` are copied to `/app` in the image
* Compiled class files under `/target/classes` are copied to `/app` in the image

### Branches
* `master`  - main branch


