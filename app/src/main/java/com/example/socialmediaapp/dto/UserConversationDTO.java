package com.example.socialmediaapp.dto;

import java.io.Serializable;

public class UserConversationDTO implements Serializable {
    private String USER_ID;
    private UserDTO USER;

    public UserConversationDTO() {
    }

    public UserConversationDTO(String USER_ID, UserDTO USER) {
        this.USER_ID = USER_ID;
        this.USER = USER;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public UserDTO getUSER() {
        return USER;
    }

    public void setUSER(UserDTO USER) {
        this.USER = USER;
    }
}
