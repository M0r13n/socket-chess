# Socket Chess
A basic chess game based on Websockets.

![alt text](https://github.com/M0r13n/socket-chess/blob/master/img/game.png "Example Game")

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
- add undo functionality
