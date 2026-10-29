package com.example.demo.entity;

import java.time.LocalDateTime;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity 
@Table (name = "chat_messeges", indexes = 
    {
        @Index (name = "idx_chat_user_id", columnList = "user_id")
    }
)
public class ChatMessage
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false, length = 1000)
    private String text;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public ChatMessage() {}

    public ChatMessage(String userId, String sender, String text) {
        this.userId = userId;
        this.sender = sender;
        this.text = text;
    }

    public Long getId() {return id;}

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
