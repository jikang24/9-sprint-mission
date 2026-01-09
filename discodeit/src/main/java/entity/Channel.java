package entity;

import java.util.UUID;

public class Channel {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String ChannelName;
    private String ChannelDescription;
    private User owner;
    private Message[] messages;

    public Channel(String ChannelName, String ChannelDescription, String User, String Message) {
        this.ChannelName = ChannelName;
        this.ChannelDescription = ChannelDescription;
        this.owner = new User(User, "", "", "");

    }


}
