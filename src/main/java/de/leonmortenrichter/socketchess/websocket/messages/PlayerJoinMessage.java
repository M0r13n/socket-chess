package de.leonmortenrichter.socketchess.websocket.messages;

import lombok.Data;

@Data
public class PlayerJoinMessage {
    private final String type = "PLAYER_JOIN";
    private String uuid;
}
