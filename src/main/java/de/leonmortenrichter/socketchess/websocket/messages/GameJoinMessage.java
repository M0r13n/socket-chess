package de.leonmortenrichter.socketchess.websocket.messages;

import lombok.Data;

@Data
public class GameJoinMessage {

    private Long gid;

    private String uuid;

    private String color;

    public GameJoinMessage() {
    }

    public GameJoinMessage(Long id, String uuid, String color) {
        this.gid = id;
        this.uuid = uuid;
        this.color = color;
    }

}
