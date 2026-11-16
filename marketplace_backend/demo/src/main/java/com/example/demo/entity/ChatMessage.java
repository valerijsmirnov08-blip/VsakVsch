package com.example.demo.entity;

import java.time.LocalDateTime;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "chat_messeges")
public class ChatMessage
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = true)
    private String userId;
    
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false, length = 1000)
    private String text;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private Users senderUser;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    public ChatMessage() {}

    public ChatMessage(String userId, String sender, String text) {
        this.userId = userId;
        this.sender = sender;
        this.text = text;
    }

    public Long getId() {return id;}

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Conversation getConversation() { return conversation; }
    public void setConversation(Conversation conversation) { this.conversation = conversation; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Users getSenderUser() { return senderUser; }
    public void setSenderUser(Users senderUser) { this.senderUser = senderUser; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
}
