Launching Application

Prerequesites: 

JDK 11

Port 8080 is open

Steps to launch server :

Step 1: Build the application

Windows: 

gradlew build

Linux:

./gradew build

Step 2: Navigate to libs directory

cd /build/libs

Step 3: Launch the application with dev profile

java -jar test-0.0.1-SNAPSHOT.jar

Endpoints: 


Swagger can be accessed at localhost:8080/swagger-ui.html

Notes:

The project is using H2 embedded database to store all the data. Meaning any time the application is restarted all the saved data will be lost.


The server is compatible with any relational database since all the queries are done using HQL.
