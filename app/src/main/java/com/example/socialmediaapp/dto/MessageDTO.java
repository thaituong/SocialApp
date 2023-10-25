package com.example.socialmediaapp.dto;

import java.io.Serializable;

public class MessageDTO implements Serializable {
    private String ID;
    private String SEND_USER_ID;
    private String CONVERSATION_ID;
    private String TYPE;
    private String CONTENT;
    private String createdAt;
    private String updatedAt;
    private int IS_SEND_USER;

    public MessageDTO(String ID, String SEND_USER_ID, String CONVERSATION_ID, String TYPE, String CONTENT, String createdAt, String updatedAt, int IS_SEND_USER) {
        this.ID = ID;
        this.SEND_USER_ID = SEND_USER_ID;
        this.CONVERSATION_ID = CONVERSATION_ID;
        this.TYPE = TYPE;
        this.CONTENT = CONTENT;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.IS_SEND_USER = IS_SEND_USER;
    }

    public MessageDTO(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSEND_USER_ID() {
        return SEND_USER_ID;
    }

    public void setSEND_USER_ID(String SEND_USER_ID) {
        this.SEND_USER_ID = SEND_USER_ID;
    }

    public String getCONVERSATION_ID() {
        return CONVERSATION_ID;
    }

    public void setCONVERSATION_ID(String CONVERSATION_ID) {
        this.CONVERSATION_ID = CONVERSATION_ID;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getIS_SEND_USER() {
        return IS_SEND_USER;
    }

    public void setIS_SEND_USER(int IS_SEND_USER) {
        this.IS_SEND_USER = IS_SEND_USER;
    }
}
