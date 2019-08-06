var stompClient = null;
var uid = uuidv4();
var gid = null;
var game = new Chess();
var pieceColor;
var board = new ChessBoard('board', {
    position: game.fen(),
    draggable: true,
    onDrop: function (from, to) {

        // from and to are encoded in uci notation, e.g. e2

        if (game.turn() !== pieceColor) {
            return 'snapback';
        }
        var move = game.move({from: from, to: to});

        if (!move) {
            return 'snapback';
        }
        makeMove(from, to);
    }
});

// ------------------------- Board Manipulation -------------------------

function updateBoard(fen) {
    game.load(fen);
    board.position(game.fen());
}

// ------------------------- API -------------------------

// Join a game
function joinRandomGame() {
    var request = $.ajax({
        url: "/api/join",
        type: "post",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            playerId: uid
        })
    });

    // create a new websocket, after we joined the game
    request.done(function (data) {
        // set game id
        gid = data.id;
        // is the player black or white ?
        if (uid === data.player1) {
            pieceColor = "w";
        } else {
            pieceColor = "b";
        }

        connect();
    });

    request.fail(function () {
        console.log("Could not join Game!");
    });
}


// ------------------------- Websocket Stuff -------------------------

// change buttons
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);

}

function makeMove(from, to) {
    stompClient.send("/chess/makeMove/" + gid, {}, JSON.stringify({'from': from, 'to': to}));
}

// Connect to a game and create a websocket
function connect() {
    var socket = new SockJS('/chess-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        stompClient.subscribe('/chess/state/' + gid, function (response) {
            console.log(response);

            // todo update board
            //updateBoard(game.fen());
        });
        stompClient.send("/chess/join/" + gid, {}, JSON.stringify({'player': uid}));
    });
}


// disconnect -- NOT YET WORKING
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}


// ------------------------- Utils -------------------------

// https://stackoverflow.com/questions/105034/create-guid-uuid-in-javascript
function uuidv4() {
    var d = new Date().getTime();
    if (typeof performance !== 'undefined' && typeof performance.now === 'function') {
        d += performance.now(); //use high-precision timer if available
    }
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
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


