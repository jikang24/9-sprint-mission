package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;


public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String userName;
    private String email;
    private String password;

    public User(String userName, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }


    public UUID getUserId() {
        return this.id;
    }

    public LocalDateTime getCreatedAtLocalDateTime(){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.createdAt)
                , java.time.ZoneId.systemDefault());
    }

    public Instant getCreatedAtInstant(){
        return Instant.ofEpochMilli(this.createdAt);
    }

    public Instant getUpdatedAtInstant(){
        return Instant.ofEpochMilli(this.updatedAt);
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

    public String getPassword() {
        return this.password;
    }

    public void updateUserName(String userName){
        this.userName = userName;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updateEmail(String email){
        this.email = email;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updatePassword(String password){
        this.password = password;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + this.id +
                ", username='" + this.userName + '\'' +
                ", email='" + this.email + '\'' +
                ", password='" + this.password + '\'' +
                '}';
    }


}
