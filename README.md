# Spring Boot Web Basics

This project serves as a foundational learning ground for mastering the basics of Spring Boot web application and REST API development. It was generated directly from Spring Initializr's API using `curl` command.

## Features
-   Basic Spring Boot setup with `spring-boot-starter-web`.
-   Project generation using `curl` command against Spring Initializr API.
-   Custom port configuration via `application.properties`.
-   Simple REST controller (`/hello`, `/greeting` endpoints).

## How to Generate & Run
1.  Generate and download the project using `curl`:
    ```bash
    curl -L [https://start.spring.io/starter.zip](https://start.spring.io/starter.zip) -o spring-boot-web-basics.zip \
      --data-urlencode "dependencies=web" \
      --data-urlencode "type=maven-project" \
      --data-urlencode "language=java" \
      --data-urlencode "bootVersion=3.3.0" \
      --data-urlencode "javaVersion=17" \
      --data-urlencode "groupId=com.example.learning" \
      --data-urlencode "artifactId=spring-boot-web-basics" \
      --data-urlencode "name=spring-boot-web-basics" \
      --data-urlencode "description=Spring Boot project for learning Web basics and REST API" \
      --data-urlencode "packageName=com.example.learning.springbootwebbasics"
    ```
2.  Unzip the downloaded file:
    `unzip spring-boot-web-basics.zip -d spring-boot-web-basics`
3.  Navigate into the project directory:
    `cd spring-boot-web-basics`
4.  Build the project using Maven Wrapper:
    `./mvnw clean install`
5.  Run the application:
    `java -jar target/spring-boot-web-basics-0.0.1-SNAPSHOT.jar`

## Endpoints
-   `GET http://localhost:8081/hello` : Returns "Hello from Spring Boot Web Basics!"
-   `GET http://localhost:8081/greeting` : Returns "Greetings, Learning World!"