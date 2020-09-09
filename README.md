# Talkdesk_Challenge
Service to manage calls

### Description
The main objective of this challenge is to implement a service to manage a specific resource: Calls. The Call resource represents a phone call between two numbers with the following attributes:

- Caller Number: the phone number of the caller.
- Callee Number: the phone number of the callee.
- Start Timestamp: start timestamp of the call.
- End Timestamp: end timestamp of the call.
- Type: Inbound or Outbound.

This challenge should have two components: a Web Service and a Client.

##### WebService
This Web Service should be able to manage and persist the Call resource, providing the following operations:

- Create Calls (one or more).
- Delete Call.
- Get all Calls using pagination and be able to filter by Type.
- Get statistics (the response to this operation should have the aggregate of the values by day, returning all days with calls):
- Total call duration by type.
- Total number of calls.
- Number of calls by Caller Number.
- Number of calls by Callee Number.
- Total call cost using the following rules:
-- Outbound calls cost 0.05 per minute after the first 5 minutes. The first 5 minutes cost 0.10.
-- Inbound calls are free.

To persist the calls you should use any database that you feel comfortable with.
#### The client
The Client should allow the programmer to use all the operations of the Web Service without having to handle the connection by himself.

### The application
#### Webserver

The webServer was developed with [springBoot] and PostgreSQL database and consists of three-layer architecture:

 - The CallController class is responsible to implement all the logic to show the data.
 - The CallService class implements all the logic of the application.
 - The Call class represents the structure model of each call and its persisting module.

#### Client
The client was developed used the [Thymeleaf] template.
The client is composed of four views 
- Ongoing calls - present only ongoing calls and has the functionality to finish the call.
- History calls - present only ended calls and it is possible to remove a specific call.
- New Call - offers a little form to insert caller, callee number, and the type of the call.
- Statistics - shows information related to calls, such as total call duration by type, number of calls by caller/callee number, the total number of calls, and total cost by type.

### Prerequisites
* [Docker]
* [Docker-compose]

### How to run it?
Clone the repository to your local machine
```sh
$ git clone https://github.com/anapcruz/Talkdesk_Challenge.git
```

An entire application can be run with a single command in a terminal:
```sh
$ docker-compose up
```

If you want to stop it use the following command:
```sh
$ docker-compose down
```

License
----

MIT


[//]: #
    
   [springBoot]: <https://spring.io/projects/spring-boot>
   [Thymeleaf]: <https://www.thymeleaf.org/>
   [Docker]: <https://www.docker.com/get-started>
   [Docker-compose]: <https://docs.docker.com/compose/install/>
