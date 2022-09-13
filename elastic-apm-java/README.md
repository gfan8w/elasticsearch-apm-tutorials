## APM sample User Microservice



#### Run the app 
1. at the current dir `elastic-apm-java` , run `docker-compose up -d`
2. start the app: at the current dir `elastic-apm-java`, then run: `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8086` , change the port you like.
3. curl sample commands
- `curl -d '{"name":"Cosmin", "email":"cosmin@email.com"}' -H "Content-Type: application/json" -X POST http://localhost:8086/api/v1//users`
- `curl -X GET http://localhost:8086/api/v1/users/{some userid}`
