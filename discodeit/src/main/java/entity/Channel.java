package entity;

import java.util.UUID;

public class Channel {
    private Long now;
    private Long createdAt;
    private Long updatedAt;
    private String channelname;
    private final UUID channelId;
    private String description;


    public Channel(String channelname, String description) {
        this.now = System.currentTimeMillis();
        createdAt = now;
        updatedAt = now;
        this.channelname = channelname;
        this.channelId = UUID.randomUUID();
        this.description = description;
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


    public UUID getChannelId() {
        return this.channelId;
    }



    public void updateChannel(String channelname) {
        this.channelname = channelname;
    }

    public String getName() {
        return channelname;
    }

    public String getDescription() {
        return description;
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
