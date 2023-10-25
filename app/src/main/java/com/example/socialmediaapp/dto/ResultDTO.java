package com.example.socialmediaapp.dto;

import java.io.Serializable;
import java.util.List;

public class ResultDTO implements Serializable {
    List<NewFeedDTO> newFeeds;
    List<CommentDTO> comments;
    List<ConversationDTO> conversations;
    List<MessageDTO> messeges;
    String token;
    UserDTO user;

    public ResultDTO(List<NewFeedDTO> newFeeds, List<CommentDTO> comments, List<ConversationDTO> conversations, List<MessageDTO> messeges, String token, UserDTO user) {
        this.newFeeds = newFeeds;
        this.comments = comments;
        this.conversations = conversations;
        this.messeges = messeges;
        this.token = token;
        this.user = user;
    }

    public List<MessageDTO> getMesseges() {
        return messeges;
    }

    public void setMesseges(List<MessageDTO> messeges) {
        this.messeges = messeges;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<ConversationDTO> getConversations() {
        return conversations;
    }

    public void setConversations(List<ConversationDTO> conversations) {
        this.conversations = conversations;
    }

    public List<NewFeedDTO> getNewFeeds() {
        return newFeeds;
    }

    public void setNewFeeds(List<NewFeedDTO> newFeeds) {
        this.newFeeds = newFeeds;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

}
