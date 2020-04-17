[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.io/#https://github.com/abstratt/sandbox/tree/calories) 

### Calories Recording Tool

[![Build Status on Travis CI](https://travis-ci.org/abstratt/sandbox.svg?branch=calories)](https://travis-ci.org/abstratt/sandbox)

##### Requirements

A REST API for the input of calories

* API Users must be able to create an account and log in.
* All API calls must be authenticated.
* Implement at least three roles with different permission levels: a regular user would only be able to CRUD on their owned records, a user manager would be able to CRUD only users, and an admin would be able to CRUD all records and users.
* Each entry has a date, time, text, and number of calories.
* If the number of calories is not provided, the API should connect to a Calories API provider (for example https://www.nutritionix.com) and try to get the number of calories for the entered meal.
* User setting - Expected number of calories per day.
* Each entry should have an extra boolean field set to true if the total for that day is less than expected number of calories per day, otherwise should be false.
* The API must be able to return data in the JSON format.
* The API should provide filter capabilities for all endpoints that return a list of elements, as well should be able to support pagination.
* The API filtering should allow using parenthesis for defining operations precedence and use any combination of the available fields. The supported operations should at least include or, and, eq (equals), ne (not equals), gt (greater than), lt (lower than). Example -> (date eq '2016-05-01') AND ((number_of_calories gt 20) OR (number_of_calories lt 10)).
Write unit tests.

##### Solution

The application was built using Spring Boot, Kotlin, Flyway, and Swagger.

The application needs Postgresql 9.x to run (but not for running the tests). It is configured to rely on the following:
- one database user: caloriesdb
- one database: caloriesdb, owned by that user

In order to build and run tests, you will need to use maven.

You can run the application using:

```mvn clean spring-boot:run```

The Swagger UI is available at: http://localhost:8090/swagger-ui.html

The API itself is at http://localhost:8090/api/...
