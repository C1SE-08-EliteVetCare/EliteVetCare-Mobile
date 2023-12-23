package com.example.elitevetcare.Model.ObjectModel;

import java.io.Serializable;
import java.util.Date;

public class Conversation implements Serializable {
    private int id;
    private Date createdAt;
    private Date updatedAt;
    private User creator;
    private User recipient;
    private Message lastMessageSent;


    public Conversation() {
    }

    public Conversation(int id, Date reopenedAt, Date lastMessageSentAt, User creator, User recipient, Message lastMessageSent) {
        this.id = id;
        this.createdAt = reopenedAt;
        this.updatedAt = lastMessageSentAt;
        this.creator = creator;
        this.recipient = recipient;
        this.lastMessageSent = lastMessageSent;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public Message getLastMessageSent() {
        return lastMessageSent;
    }

    public void setLastMessageSent(Message lastMessageSent) {
        this.lastMessageSent = lastMessageSent;
    }
}