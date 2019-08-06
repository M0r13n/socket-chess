package de.leonmortenrichter.socketchess.websocket.messages;

public class GameUpdateMessage {
    private String fen;

    public GameUpdateMessage() {
    }

    public GameUpdateMessage(String fen) {
        this.fen = fen;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    @Override
    public String toString() {
        return "GameUpdateMessage{" +
                "fen='" + fen + '\'' +
                '}';
    }
}
