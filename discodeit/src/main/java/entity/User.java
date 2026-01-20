package entity;


import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String userName;
    private String email;
    private String phoneNumber;

    public User(String userName, String email, String phoneNumber) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


    public UUID getUserId() {
        return this.id;
    }

    public LocalDateTime getCreatedAtLocalDateTime(){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.createdAt)
                , java.time.ZoneId.systemDefault());
    }

    public LocalDateTime getUpdatedAtLocalDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.updatedAt)
        , java.time.ZoneId.systemDefault());
    }

    public String getUserName() {
        return this.userName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void updateUserName(String userName){
        this.userName = userName;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updateEmail(String email){
        this.email = email;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updatePhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + this.id +
                ", username='" + this.userName + '\'' +
                ", email='" + this.email + '\'' +
                ", phonenumber='" + this.phoneNumber + '\'' +
                '}';
    }


}
