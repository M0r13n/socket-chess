package de.leonmortenrichter.socketchess.chess;

import lombok.Data;


@Data
public class PlayerModel {

    private String playerId;

    PlayerModel() {
    }

    PlayerModel(String playerId) {
        this.playerId = playerId;
    }
}
