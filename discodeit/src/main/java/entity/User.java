package entity;

import java.util.UUID;

public class User {
    private final UUID id;
    private Long now;
    private Long createdAt;
    private Long updatedAt;
    private String username;
    private String email;
    private String phonenumber;

    public User(String username, String email, String phonenumber) {
        this.id = UUID.randomUUID();
        this.now = System.currentTimeMillis();
        createdAt = now;
        updatedAt = now;
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public UUID getId() {
        return this.id;
    }

    public Long getCreatedAt() {
        return this.createdAt;
    }

    public Long getUpdatedAt() {
        return this.updatedAt;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhonenumber() {
        return this.phonenumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + this.id +
                ", username='" + this.username + '\'' +
                ", email='" + this.email + '\'' +
                ", phonenumber='" + this.phonenumber + '\'' +
                '}';
    }

    public void updateUser(String username, String email, String phonenumber){
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
    }

}
