## Spring Reactive User Microservice
#### Dependencies:
 - Spring WebFlux


#### Run the app 
1. go to `cd ../elastic-apm-java/docker`  , run `docker-compose up -d`
2. start the app: at the current dir, then run: `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8085` , change the port you like.
3. curl sample commands
- `curl -d '{"name":"Cosmin", "email":"cosmin@email.com"}' -H "Content-Type: application/json" -X POST http://localhost:8085/users`
- `curl -X GET http://localhost:8085/users/{some userid}`
- `curl -X GET http://localhost:8085/users`  return all users.
