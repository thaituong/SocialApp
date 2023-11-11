package com.example.socialmediaapp.dto;

import com.example.socialmediaapp.dto.ConversationDetailDTO;

import java.io.Serializable;

public class ConversationDTO implements Serializable {
    private ConversationDetailDTO CONVERSATION;

    public ConversationDTO() {
    }

    public ConversationDTO(ConversationDetailDTO CONVERSATION) {
        this.CONVERSATION = CONVERSATION;
    }

    public ConversationDetailDTO getCONVERSATION() {
        return CONVERSATION;
    }

    public void setCONVERSATION(ConversationDetailDTO CONVERSATION) {
        this.CONVERSATION = CONVERSATION;
    }
}
