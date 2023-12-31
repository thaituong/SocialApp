package com.example.socialmediaapp.dto;

import java.io.Serializable;
import java.util.List;

public class ResultDTO implements Serializable {
    List<NewFeedDTO> newFeeds;
    List<CommentDTO> comments;
    List<ConversationDTO> conversations;
    List<NotificationDTO> notifications;
    List<MessageDTO> messeges;
    List<UserDTO> users;
    String token;
    UserDTO user;
    String conversation_id;

    public ResultDTO(List<NewFeedDTO> newFeeds, List<CommentDTO> comments, List<ConversationDTO> conversations, List<NotificationDTO> notifications, List<MessageDTO> messeges, List<UserDTO> users, String token, UserDTO user, String conversation_id) {
        this.newFeeds = newFeeds;
        this.comments = comments;
        this.conversations = conversations;
        this.notifications = notifications;
        this.messeges = messeges;
        this.users = users;
        this.token = token;
        this.user = user;
        this.conversation_id = conversation_id;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
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
