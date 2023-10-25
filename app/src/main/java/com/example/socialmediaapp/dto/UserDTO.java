package com.example.socialmediaapp.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private String ID;
    private String USERNAME;
    private String FULLNAME;
    private String AVATAR;
    private String FOLLOWING;
    private String FOLLOWERS;
    private String POSTS;
    private String EMAIL;

    public UserDTO(String ID, String USERNAME, String FULLNAME, String AVATAR, String FOLLOWING, String FOLLOWERS, String POSTS, String email) {
        this.ID = ID;
        this.USERNAME = USERNAME;
        this.FULLNAME = FULLNAME;
        this.AVATAR = AVATAR;
        this.FOLLOWING = FOLLOWING;
        this.FOLLOWERS = FOLLOWERS;
        this.POSTS = POSTS;
        this.EMAIL = email;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getFULLNAME() {
        return FULLNAME;
    }

    public void setFULLNAME(String FULLNAME) {
        this.FULLNAME = FULLNAME;
    }

    public String getAVATAR() {
        return AVATAR;
    }

    public void setAVATAR(String AVATAR) {
        this.AVATAR = AVATAR;
    }

    public String getFOLLOWING() {
        return FOLLOWING;
    }

    public void setFOLLOWING(String FOLLOWING) {
        this.FOLLOWING = FOLLOWING;
    }

    public String getFOLLOWERS() {
        return FOLLOWERS;
    }

    public void setFOLLOWERS(String FOLLOWERS) {
        this.FOLLOWERS = FOLLOWERS;
    }

    public String getPOSTS() {
        return POSTS;
    }

    public void setPOSTS(String POSTS) {
        this.POSTS = POSTS;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }
}
