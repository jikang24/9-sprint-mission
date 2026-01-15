package entity;

import java.util.UUID;

public class Message {
    private final UUID messageId;
    private Long now;
    private Long createdAt;
    private Long updatedAt;
    private User username;
    private String messagetext;
    private final UUID channelId;
    private final UUID userId;



    public Message(UUID userId, UUID channelId, String messagetext) {
        this.messageId = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.now = System.currentTimeMillis();
        createdAt = now;
        updatedAt = now;
        this.messagetext = messagetext;
    }


    public UUID getMessageId() {
        return messageId;
    }

    public Long getNow() {
        return now;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public User getUsername() {
        return username;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public String getMessagetext() {
        return messagetext;
    }

    public void updateMessage(String messagetext){
        this.messagetext = messagetext;
    }


}




