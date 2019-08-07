package de.leonmortenrichter.socketchess.websocket.messages;

import lombok.Data;

@Data
public class PlayerJoinMessage {
    private String uuid;
}
