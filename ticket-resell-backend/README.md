# Backend of the Ticket-Reselling Application

The backend of the application consists on a SpringBoot Application that exposes and RESTFul API.

## Execution

To execute the backend application, from within then backend project directory (`ticket-resell-backend`), run:

```bash
mvn package && java -jar target\ticket-resell-backend-0.0.1-SNAPSHOT.jar
```

If inside an IDE, just run the `TicketResellBackendApplication` class.

## API Endpoints

The API is organized in resources. In the next sections the several resource endpoints will be covered and explained. 
Keep in mind that not all possible request options have been considered.
Therefore, it could happen that, although unlikely, an incorrect use of the API could lead to unpredictable errors.
We recommend following the examples provided to avoid unexpected issues. 

### User

`POST /users`

Allows the creation of users. The application does not support user registration either login.
We also assume that a user already has in their possession an Ethereum address and key.
Therefore, these values have to be provided. An user without an Ethereum address or key won't be able to purchase.

**Request body example:**
```json
 {
    "name": "Jon",
    "ethAddress": "0x8890E0194Aa8c322ac3f883c52AeBF658DF702Ac",
    "ethKey": "be61fec3bee28fa72968ec5c2f63eecde195eefa9e3328a28d62836c85f1f287",
    "organizer": false
}
```

`GET /users` 

Returns the list of all registered users.

`GET /users/{id}`

Returns the user whose `id` is the one provided in the URL path. If no user exists, a `404 HTTP` error will be returned.

`GET /users?organizer={true|false}`

Returns a list of users that are either organizers or not, depending on the query param provided.

### Events

`POST /events`

Allows the creation of events. The fields `name`, `date` and `place` are self explanatory.
The field `price` is used to specify which will be the price for the tickets of this event.
The field `nTickets` is used to specify how many tickets should be created for this event.
Finally, `organizerId` is the `id` of the user that creates the event and that will be the initial owner of all the 
tickets of the event.

**Request body example:**
```json
{
	"name": "Blockchain event",
	"date": "2020-03-01T20:00:00",
	"place": "TU Berlin",
	"price": 55,
	"ntickets": 4,
	"organizerId": 2
}
```

`GET /events`

Returns the list of all events.

`GET /events/{id}`

Returns the event whose `id` is the one provided in the URL path. If no event exists, a `404 HTTP` error will be 
returned.

### Ticket

As you will see, there is no endpoint for ticket creation. The tickets will be automatically created when an event is 
created.

`GET /tickets`

Returns the list of all tickets.

`GET /tickets?eventId={id}&onSale={true|false}`

Returns a list of tickets from a specific event that are either on sale or not.
This endpoint is useful to retrieve the list of tickets that can be purchased for an specific event.

`PATCH /tickets/{id}`

Allows the update of the ticket specified with the `id` URL path variable.
The fields that can be updated are `onSale` or `price`.
It's possible to update both fields at the same time or only one at each time.
When updating both fields, it's recommended to do so in the same request.
Depending on the update and the ticket state, updating the fields in different requests may incur in an extra blockchain
 gas cost.

**Request body example:**
```json
{
	"onSale": true,
	"price": 50
}
```

### Transactions

The way of purchasing tickets is by creating transactions.

`POST /transactions`

Allows the creation of transactions and, therefore, the purchase of tickets.
A transaction is defined by the fields `buyerId`, which is the `id` of the user that wants to buy a ticket, 
and `ticketId`, which is the `id` of the ticket that the buyer want to purchase.

**Request body example:**
```json
{
	"buyerId": 3,
	"tickedId": 32
}
```

`GET /transactions`

Returns a list of all the transactions that have been performed.

## Relational Database and Blockchain

The database server that supports the application is a MySQL instance hosted in AWS. 
The connection details can be found in the `/src/main/resources/application.properties` file.

While the database is accessible for everyone, the blockchain used for the development was deployed locally.
Therefore, every Ethereum address that are already stored in that database won't work for everyone. 

To deploy the blockchain locally, we have used [Ganache](https://www.trufflesuite.com/ganache).