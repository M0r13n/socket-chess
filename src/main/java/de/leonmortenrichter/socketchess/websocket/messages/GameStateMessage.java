package de.leonmortenrichter.socketchess.websocket.messages;

import de.leonmortenrichter.socketchess.chess.GameModel;
import lombok.Data;

@Data
public class GameStateMessage {
    private final String TYPE = "GAME_STATE";

    private String fen;
    private Boolean currentPlayer;
    private Boolean hasEmptySlot;

    public GameStateMessage() {
    }

    public GameStateMessage(GameModel game) {
        this.fen = game.getFen();
        this.currentPlayer = game.getCurrentPlayer();
        this.hasEmptySlot = game.getHasEmptySlot();
    }
}
