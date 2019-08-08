/**
 * General:
 *
 * Client and Server communicate via STOMP messages through a websocket.
 * Therefore the client uses sock.js.
 *
 * Chess data is always encoded in the official UCI notation.
 *
 * Note:
 * Each player is identified by a unique UUID and needs to send it with every request.
 * */

// variables
var stompClient;
var gid;
var pieceColor;
var uuid;
var canMove = false;
var game = new Chess();
var board = new ChessBoard('board', {
    position: game.fen(),
    draggable: true,
    onDrop: function (from, to) {

        if ((game.turn() !== pieceColor) || (!canMove)) {
            return 'snapback';
        }
        var move = game.move({from: from, to: to});
        if (!move) {
            return 'snapback';
        }
        makeMove(from, to);
    }
});

// ------------------------- Page Stuff -------------------------

function updateBoard(fen) {
    game.load(fen);
    board.position(game.fen());
}

function rotate() {
    if (board.orientation().charAt(0) !== pieceColor)
        board.flip();

}

// change buttons
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);

}

// ------------------------- API -------------------------

// Join a game
function joinRandomGame() {
    var request = $.ajax({
        url: "/api/join",
        type: "post",
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    });

    // create a new websocket, after we joined the game
    request.done(function (data) {
        uuid = data.uuid;
        gid = data.gid;
        pieceColor = data.color;
        rotate();
        connect();
    });

    request.fail(function () {
        console.log("Could not join Game!");
    });
}


// ------------------------- Websocket Stuff -------------------------


function makeMove(from, to) {
    stompClient.send("/chess/makeMove/" + gid, {}, JSON.stringify({'from': from, 'to': to, 'uuid': uuid}));
}

// Connect to a game and create a websocket
function connect() {
    // create websocket
    var socket = new SockJS('/chess-websocket');
    stompClient = Stomp.over(socket);

    // connect socket to server
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        // subscribe to endpoints
        stompClient.subscribe('/chess/state/' + gid, function (event) {
            handleGameEvent(event);
        });
        stompClient.send("/chess/join/" + gid, {}, JSON.stringify({'uuid': uuid}));
    });
}

function handleGameEvent(event) {
    var msg = JSON.parse(event.body);

    switch (msg.type) {
        case "GAME_STATE":
            if ((msg.hasEmptySlot)) {
                canMove = false;
                return;
            }
            updateBoard(msg.fen);
            canMove = true;
            break;

        case "GAME_DISCONNECT":
            console.log("Player left the game!");
            canMove = false;
            break;

        default:
            console.log("Could not parse data!");
    }

}

// disconnect
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        joinRandomGame();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
});


