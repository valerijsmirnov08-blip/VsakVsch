package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.ChatMessage;
import com.example.demo.entity.Conversation;
import java.util.List;
import java.util.Map;

import com.example.demo.service.MessengerService;

@RestController
@RequestMapping("/api/messenger")
@CrossOrigin(origins = "http://localhost:5173")
public class MessengerController
{
    @Autowired 
    private MessengerService messengerService;

    @GetMapping ("/conversations")
    public List<Conversation> getConversation(@RequestParam Long userId)
    {
        return messengerService.getUserConversations(userId);
    }
    @PostMapping ("/open/product")
    public Conversation openProductChat(@RequestParam Long buyerId, @RequestParam Long vendorId, @RequestParam Long productId)
    {
        return messengerService.openProductChat(buyerId, vendorId, productId);
    }
    @PostMapping ("/open/support")
    public Conversation openSupportChat(@RequestParam(required = false) Long buyerId, @RequestParam(required = false) String guestEMail)
    {
        return messengerService.openSupportChat(buyerId, guestEMail);
    }
    @GetMapping ("/{conversationId}/messages")
    public List<ChatMessage> getMessages(@PathVariable Long conversationId, @RequestParam (required = false) Long readerId)
    {
        return messengerService.getChatMessages(conversationId, readerId);
    }
    @PostMapping ("/{conversationId}/messages")
    public ChatMessage sendMessages(@PathVariable Long conversationId, @RequestBody Map<String, Object> payload)
    {
        Long senderId = payload.get("senderId") != null ? Long.valueOf(payload.get("senderId").toString()) : null;
        String text = payload.get("text").toString();
        return messengerService.sendMessage(conversationId, senderId, text);
    }
}
