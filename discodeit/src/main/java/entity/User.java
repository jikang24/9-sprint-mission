package entity;

import java.util.UUID;

public class User {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String nickname;

    public User(String displayName, String email, String phoneNumber, String nickname) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nickname = UUID.randomUUID().toString();
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getNickname() {
        return nickname;
    }
//    public User update(String displayName, String email, String phoneNumber) {
//        return new User(displayName, email, phoneNumber);
//    }
// TODO 업데이트 메서드 추가

    //  무언가 id (무언가 파라미터)

//    public boolean u

}
