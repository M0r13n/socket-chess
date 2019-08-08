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

import $ from 'jquery';
import { ChessBoard, ChessBoardInstance, BoardConfig } from 'chessboardjs';
import * as Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { Chess, ChessInstance } from 'chess.js';

// variables
let stompClient: Stomp.Client;
let game: ChessInstance = new Chess();

let gid;
let pieceColor;
let uuid;
let canMove = false;

const config: BoardConfig = {
  position: game.fen(),
  draggable: true,
  // @ts-ignore
  onDrop: (from, to) => {
    if (game.turn() !== pieceColor || !canMove) {
      return;
    }
    const move = game.move({ from: from, to: to });
    if (!move) {
      return;
    }
    makeMove(from, to);
  }
};

const board: ChessBoardInstance = ChessBoard('board', config);

// ------------------------- Page Stuff -------------------------

function updateBoard(fen) {
  game.load(fen);
  board.position(game.fen());
}

// ------------------------- API -------------------------

// Join a game
function joinRandomGame() {
  const request = $.ajax({
    url: '/api/join',
    type: 'post',
    contentType: 'application/json; charset=utf-8',
    dataType: 'json'
  });

  // create a new websocket, after we joined the game
  request.done(data => {
    uuid = data.uuid;
    gid = data.gid;
    pieceColor = data.color;

    connect();
  });

  request.fail(() => {
    console.log('Could not join Game!');
  });
}

// ------------------------- Websocket Stuff -------------------------

// change buttons
function setConnected(connected) {
  $('#connect').prop('disabled', connected);
  $('#disconnect').prop('disabled', !connected);
}

function makeMove(from, to) {
  stompClient.send(
    '/chess/makeMove/' + gid,
    {},
    JSON.stringify({ from: from, to: to, uuid: uuid })
  );
}

// Connect to a game and create a websocket
function connect() {
  const socket = new SockJS('/chess-websocket');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, frame => {
    setConnected(true);
    console.log('Connected: ' + frame);

    stompClient.subscribe('/chess/state/' + gid, data => {
      // update board
      const gameData = JSON.parse(data.body);
      if (gameData.hasEmptySlot) {
        canMove = false;
      } else {
        updateBoard(gameData.fen);
        canMove = true;
      }
    });
    stompClient.send('/chess/join/' + gid, {}, JSON.stringify({ uuid: uuid }));
  });
}

// disconnect
function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect(() => {
      setConnected(false);
      console.log('Disconnected');
    });
  }
}

$(function() {
  $('form').on('submit', e => {
    e.preventDefault();
  });
  $('#connect').click(() => {
    joinRandomGame();
  });
  $('#disconnect').click(() => {
    disconnect();
  });
});
