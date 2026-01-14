package entity;

import java.util.UUID;

public class Channel {
    private Long now;
    private Long createdAt;
    private Long updatedAt;
    private String channelname;
    private String username;
    private UUID id;



    public Channel() {
        this.now = System.currentTimeMillis();
        createdAt = now;
        updatedAt = now;
        this.channelname = channelname;
        this.username = username;
        this.id = UUID.randomUUID();

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

    public String getUsername() {
        return username;
    }

    public UUID getId() {
        return id;
    }

    public String getChannelname() {
        return channelname;
    }

    public void updateChannel(String channelname){
        this.channelname = channelname;
    }

    public CharSequence getName() {
        return username;
    }

    public CharSequence getDescription() {
        return channelname;
    }

//    public void plusUser(String username){
//        this.username += username;
//    }
//
//    public void minusUser(String username){
//        this.username = this.username.replace(username, "");
//    }
}
