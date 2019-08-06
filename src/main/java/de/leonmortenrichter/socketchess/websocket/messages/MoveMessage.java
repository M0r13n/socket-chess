package de.leonmortenrichter.socketchess.websocket.messages;

public class MoveMessage {
    private String from;
    private String to;

    public MoveMessage() {

    }

    public MoveMessage(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "MoveMessage{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
