# Socket Chess
A basic chess game based on Websockets.

## Run
- build: `gradle clean build`
- run: `java -jar build/libs/socket-chess-0.0.1-SNAPSHOT.jar`


## Server
- written in pure Java using the Spring Framework
- REST Endpoint for pre-game data exchange, e.g. join a game
- Spring STOMP Websocket


## Client
- Written in Javascript
- using SockJS

## Ideas for improvement
- enhance UI
- make games watchable
- dockerize
- add undo functionality
