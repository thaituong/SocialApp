package com.example.socialmediaapp.dto;

import java.io.Serializable;
import java.util.List;

public class ConversationDetailDTO implements Serializable {
    private String ID;
    private String TITLE;
    private List<UserConversationDTO> USER_CONVERSATIONs;

    public ConversationDetailDTO() {
    }

    public ConversationDetailDTO(String ID, String TITLE, List<UserConversationDTO> USER_CONVERSATIONs) {
        this.ID = ID;
        this.TITLE = TITLE;
        this.USER_CONVERSATIONs = USER_CONVERSATIONs;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public List<UserConversationDTO> getUSER_CONVERSATIONs() {
        return USER_CONVERSATIONs;
    }

    public void setUSER_CONVERSATIONs(List<UserConversationDTO> USER_CONVERSATIONs) {
        this.USER_CONVERSATIONs = USER_CONVERSATIONs;
    }
}
