package entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID messageId;
    private final Long createdAt;
    private Long updatedAt;
    private String messageText;
    private final UUID channelId;
    private final UUID userId;


    public Message(UUID userId, UUID channelId, String messageText) {

        this.messageId = UUID.randomUUID();
        this.userId = userId;
        this.channelId = channelId;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.messageText = messageText;
    }

    public UUID getMessageId() {
        return messageId;
    }

//    public User getUsername() {
//        return username;
//    }

    public LocalDateTime getCreatedAtLocalDateTime(){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.createdAt)
                , java.time.ZoneId.systemDefault());
    }

    public LocalDateTime getUpdatedAtLocalDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.updatedAt)
                , java.time.ZoneId.systemDefault());
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void updateMessage(String messageText){
        this.messageText = messageText;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", messagetext='" + messageText + '\'' +
                ", channelId=" + channelId +
                ", userId=" + userId +
                '}';
    }
}




