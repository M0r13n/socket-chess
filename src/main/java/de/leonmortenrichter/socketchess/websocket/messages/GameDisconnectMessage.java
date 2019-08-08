package de.leonmortenrichter.socketchess.websocket.messages;

import lombok.Data;

@Data
public class GameDisconnectMessage {
    private final String TYPE = "GAME_DISCONNECT";
    private String player;

    public GameDisconnectMessage() {
    }

    public GameDisconnectMessage(String player) {
        this.player = player;
    }
}
