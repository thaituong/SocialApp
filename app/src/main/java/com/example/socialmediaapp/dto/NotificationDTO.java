package com.example.socialmediaapp.dto;

import java.io.Serializable;

public class NotificationDTO implements Serializable {
    private int ID;
    private String USER_ID;
    private String POST_ID;
    private String R_USER_ID;
    private String TYPE;
    private String createdAt;
    private String updatedAt;
    private UserDTO USER;

    public NotificationDTO(int ID, String USER_ID, String POST_ID, String r_USER_ID, String TYPE, String createdAt, String updatedAt, UserDTO USER) {
        this.ID = ID;
        this.USER_ID = USER_ID;
        this.POST_ID = POST_ID;
        R_USER_ID = r_USER_ID;
        this.TYPE = TYPE;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.USER = USER;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getPOST_ID() {
        return POST_ID;
    }

    public void setPOST_ID(String POST_ID) {
        this.POST_ID = POST_ID;
    }

    public String getR_USER_ID() {
        return R_USER_ID;
    }

    public void setR_USER_ID(String r_USER_ID) {
        R_USER_ID = r_USER_ID;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserDTO getUSER() {
        return USER;
    }

    public void setUSER(UserDTO USER) {
        this.USER = USER;
    }
}
