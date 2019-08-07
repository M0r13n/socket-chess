package de.leonmortenrichter.socketchess.chess;


import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class GameModel {

    @Id
    @GeneratedValue
    public Long id;

    private String player1;
    private String player2;

    private String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    GameModel() {

    }


    GameModel(String player) {
        this.player1 = player;
    }

    public boolean makeMove(String from, String to) {
        Board board = new Board();
        board.loadFromFen(this.fen);
        Square fromSquare = Square.fromValue(from.toUpperCase());
        Square toSquare = Square.fromValue(to.toUpperCase());
        // only update the board if the move is legal
        if (board.doMove(new Move(fromSquare, toSquare))) {
            this.fen = board.getFen();
            return true;
        }
        return false;

    }
}
