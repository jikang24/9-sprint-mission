package entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class Channel {
    private final Long createdAt;
    private Long updatedAt;
    private String channelName;
    private final UUID channelId;
    private String description;


    public Channel(String channelName, String description) {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.channelName = channelName;
        this.channelId = UUID.randomUUID();
        this.description = description;
    }

//
//    public Long getCreatedAt() {
//        return createdAt;
//    }
//
//    public Long getUpdatedAt() {
//        return updatedAt;
//    }

    public LocalDateTime getCreatedAtLocalDateTime(){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.createdAt)
                , java.time.ZoneId.systemDefault());
    }

    public LocalDateTime getUpdatedAtLocalDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.updatedAt)
                , java.time.ZoneId.systemDefault());
    }


    public UUID getChannelId() {
        return this.channelId;
    }



    public void updateChannel(String channelName) {
        this.channelName = channelName;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updateDescription(String description){
        this.description = description;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getChannelName() {
        return channelName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Channel{" +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", channelname='" + channelName + '\'' +
                ", channelId=" + channelId +
                ", description='" + description + '\'' +
                '}';
    }

    //    public void plusUser(String username){
//        this.username += username;
//    }
//
//    public void minusUser(String username){
//        this.username = this.username.replace(username, "");
//    }
    //    public String getChannelname() {
//        return this.channelname;
//    }
}
