package com.example.socialmediaapp.dto;

import java.io.Serializable;
import java.util.List;

public class CommentDTO implements Serializable {
    private String ID;
    private String POST_ID;
    private String CONTENT;
    private String createdAt;
    private List<CommentDTO> COMMENTs;
    private UserDTO USER;

    public CommentDTO(String ID, String POST_ID, String CONTENT, String createdAt, List<CommentDTO> COMMENTs, UserDTO USER) {
        this.ID = ID;
        this.POST_ID = POST_ID;
        this.CONTENT = CONTENT;
        this.createdAt = createdAt;
        this.COMMENTs = COMMENTs;
        this.USER = USER;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPOST_ID() {
        return POST_ID;
    }

    public void setPOST_ID(String POST_ID) {
        this.POST_ID = POST_ID;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<CommentDTO> getCOMMENTs() {
        return COMMENTs;
    }

    public void setCOMMENTs(List<CommentDTO> COMMENTs) {
        this.COMMENTs = COMMENTs;
    }

    public UserDTO getUSER() {
        return USER;
    }

    public void setUSER(UserDTO USER) {
        this.USER = USER;
    }
}
