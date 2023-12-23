package com.example.elitevetcare.Model.ObjectModel;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private int id;
    private String content;
    private User author;
    private int conversationId;
    private Date createdAt;

    public Message() {
    }

    public Message(int id, String content, User author, int conversationId, Date createdAt) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.conversationId = conversationId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

}