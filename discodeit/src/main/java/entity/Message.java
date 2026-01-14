package entity;

import java.util.UUID;

public class Message {
    private final UUID id;
    private Long now;
    private Long createdAt;
    private Long updatedAt;
    private String username;
    private String message;
    private String reactcion;
    private UUID channelId;
    private UUID userId;

    public Message(String username) {
        this.username = username;
        this.id = UUID.randomUUID();
        this.now = System.currentTimeMillis();
        createdAt = now;
        updatedAt = now;
        this.message = message;
        this.reactcion = reactcion;
        this.channelId = channelId;
        this.userId = userId;
    }
}
