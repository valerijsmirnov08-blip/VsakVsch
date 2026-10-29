package com.example.demo.controller;

import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.service.BotService;
import com.example.demo.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;
@RestController 
@RequestMapping ("/api/chat")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController
{
    @Autowired 
    private ChatMessageRepository chatMessageRepository;
    @Autowired 
    private BotService botService;

    @PostMapping ("/bot")
    public ResponseEntity<Map<String, String>> handleBotMessege(@RequestBody ChatRequest payload)
    {
        String reply = botService.procceseAndSaveMessege(payload.getUserId(), payload.getMessage() );
        return ResponseEntity.ok(Map.of("reply", reply));
    }
    @GetMapping ("/history/{userId}")
    public ResponseEntity<List<ChatMessage>> getHistory(@PathVariable String userId)
    {
        return ResponseEntity.ok(chatMessageRepository.findByUserIdOrderByCreatedAtAsc(userId));
    }
}
