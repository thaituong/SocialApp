package com.example.socialmediaapp.dto;

import java.io.Serializable;
import java.util.List;

public class NewFeedDTO implements Serializable {
    private String ID;
    private String CAPTION;
    private String LIKES;
    private String COMMENTS;
    private String ISLIKED;
    private String createdAt;
    private List<PostImage> POST_IMAGEs;
    private UserDTO USER;

    public NewFeedDTO(String ID, String CAPTION, String LIKES, String COMMENTS, String ISLIKED, String createdAt, List<PostImage> POST_IMAGEs, UserDTO USER) {
        this.ID = ID;
        this.CAPTION = CAPTION;
        this.LIKES = LIKES;
        this.COMMENTS=COMMENTS;
        this.ISLIKED = ISLIKED;
        this.createdAt = createdAt;
        this.POST_IMAGEs = POST_IMAGEs;
        this.USER = USER;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCAPTION() {
        return CAPTION;
    }

    public void setCAPTION(String CAPTION) {
        this.CAPTION = CAPTION;
    }

    public String getLIKES() {
        return LIKES;
    }

    public void setLIKES(String LIKES) {
        this.LIKES = LIKES;
    }

    public String getISLIKED() {
        return ISLIKED;
    }

    public void setISLIKED(String ISLIKED) {
        this.ISLIKED = ISLIKED;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<PostImage> getPOST_IMAGEs() {
        return POST_IMAGEs;
    }

    public void setPOST_IMAGEs(List<PostImage> POST_IMAGEs) {
        this.POST_IMAGEs = POST_IMAGEs;
    }

    public UserDTO getUSER() {
        return USER;
    }

    public void setUSER(UserDTO USER) {
        this.USER = USER;
    }

    public String getCOMMENTS() {
        return COMMENTS;
    }

    public void setCOMMENTS(String COMMENTS) {
        this.COMMENTS = COMMENTS;
    }
}
