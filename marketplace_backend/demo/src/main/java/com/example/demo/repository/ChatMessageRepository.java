package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ChatMessage;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>
{
    List<ChatMessage> findByUserIdOrderByCreatedAtAsc(String userId);
}
