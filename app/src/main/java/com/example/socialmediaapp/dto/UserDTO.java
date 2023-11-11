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
    private String GENDER;
    private String MOBILE;
    private String ADDRESS;
    private String DESCRIPTION;
    private String ISFOLLOWED;

    public UserDTO(String FULLNAME, String AVATAR) {
        this.FULLNAME = FULLNAME;
        this.AVATAR = AVATAR;
    }

    public UserDTO(String ID, String USERNAME, String FULLNAME, String AVATAR, String FOLLOWING, String FOLLOWERS, String POSTS, String EMAIL, String GENDER, String MOBILE, String ADDRESS, String DESCRIPTION, String ISFOLLOWED) {
        this.ID = ID;
        this.USERNAME = USERNAME;
        this.FULLNAME = FULLNAME;
        this.AVATAR = AVATAR;
        this.FOLLOWING = FOLLOWING;
        this.FOLLOWERS = FOLLOWERS;
        this.POSTS = POSTS;
        this.EMAIL = EMAIL;
        this.GENDER = GENDER;
        this.MOBILE = MOBILE;
        this.ADDRESS = ADDRESS;
        this.DESCRIPTION = DESCRIPTION;
        this.ISFOLLOWED = ISFOLLOWED;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getISFOLLOWED() {
        return ISFOLLOWED;
    }

    public void setISFOLLOWED(String ISFOLLOWED) {
        this.ISFOLLOWED = ISFOLLOWED;
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
