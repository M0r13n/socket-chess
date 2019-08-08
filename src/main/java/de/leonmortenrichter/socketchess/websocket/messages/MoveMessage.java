package de.leonmortenrichter.socketchess.websocket.messages;

import lombok.Data;

@Data
public class MoveMessage {
    private final String TYPE = "MOVE";
    private String from;
    private String to;
    private String uuid;

    public MoveMessage() {
    }

}
