# TicketResell

To execute the backend application, first clone the project and then, from within then backend project directory, run:

```bash
mvn package && java -jar target\ticket-resell-backend-0.0.1-SNAPSHOT.jar
```

If in an IDE, just run the `TicketResellBackendApplication` class.

## REST Resources and API Endpoints

Currently available, there are the following resources with the following endpoints:
* Home
    * `GET /`
    * `GET /home`

## Spring Documentation

To learn more about the Spring Framework, check its [guides](https://spring.io/guides).

Some of the guides used for setting up the basics are:
1. To set up a basic Spring Boot application: [Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot/)
2. To create a basic controller that returns a simple text: [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
3. To understand data access with Spring JPA, basic POJO, Entities, and Repositories: [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
4. To return data in JSON format: [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
5. To set up the MySQL Database, connect, and interact with it: [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

## Ethereum
### Contracts
Requires to install solidity locally (solc).
Add contracts under /java/main/resources and run `mvn clean package`
### Test 
To test you will need [Ganache](https://www.trufflesuite.com/ganache) 
After installing create a new workspace and make sure to edit your RPC server URL in resources/application.properties for ethereum.connection.url
Also make sure to create accounts which should be unlocked. Once ganache is running you can see the list of accounts and you can also get their private keys to test