# Dolly URL Shortener

## Introduction

This is a Java application for the Dolly URL Shortener. 
The service performs the following services:

* exposes API endpoints to create short urls and get long urls
* redirects short urls to long urls
* stores short url and long url in Mongo data store

***

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Dependencies
* Docker
* MongoDB
* Java 11+
* Maven 3.5.4

### Setting up MongoDB

This project relies on a running instance of MongoDB which is provided by running the Dockerfile:
Run the following command on the root of the project:
```
docker build . -t mongo
```

and run the container with:
```
docker run -p 27017:27017 mongo
```

This will start MongoDB on port 27017.
### Building Web Server

This project uses Maven for dependency management and for building JAR.

```
mvn clean package
```
will generate a SNAPSHOT.jar file in the /target directory. 
To execute the jar run:

```
java -jar target/dolly-shortener-0.0.1-SNAPSHOT.jar
```
***

This will start the web server on port 8080

### Making requests
There are two endpoints exposed on localhost:

**1. Get long url**

`GET /url/{hash}`

where `hash` is the 7 characters generated when shortening the long url.
This will redirect you to the provided long url.

**2. Create shortened url**

`POST /url`

with the body accepting plain/text. It returns a shortened url.

### Tests

To execute the tests run:
```
mvn clean test
```