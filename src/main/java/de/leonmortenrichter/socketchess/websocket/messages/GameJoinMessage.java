package de.leonmortenrichter.socketchess.websocket.messages;

import lombok.Data;

@Data
public class GameJoinMessage {

    private final String TYPE = "GAME_JOIN";

    private Long gid;

    private String uuid;

    private String color;

    private Boolean hasEmptySlot;

    public GameJoinMessage() {
    }

    public GameJoinMessage(Long id, String uuid, String color, boolean hasEmptySlot) {
        this.gid = id;
        this.uuid = uuid;
        this.color = color;
        this.hasEmptySlot = hasEmptySlot;
    }

}
