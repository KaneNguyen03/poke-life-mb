package prm392.project.model;

public class ChatMessage {
    private String sender;
    private String text;
    private String userId;

    public ChatMessage(String sender, String text, String userId) {
        this.sender = sender;
        this.text = text;
        this.userId = userId;
    }

    // Getters and Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
