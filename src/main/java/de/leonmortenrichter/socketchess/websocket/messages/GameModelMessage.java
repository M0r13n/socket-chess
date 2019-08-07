package de.leonmortenrichter.socketchess.websocket.messages;

import de.leonmortenrichter.socketchess.chess.GameModel;
import lombok.Data;

@Data
public class GameModelMessage {
    private String fen;
    private Boolean currentPlayer;

    public GameModelMessage(){}

    public GameModelMessage(GameModel game){
        this.fen = game.getFen();
        this.currentPlayer=game.getCurrentPlayer();
    }
}
