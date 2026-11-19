package com.example.demo.controller;

import com.example.demo.entity.ChatMessage;
import com.example.demo.service.MessengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class MessengerWsController {

    @Autowired
    private MessengerService messengerService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{conversationId}/send")
    public void sendMessage(
            @DestinationVariable Long conversationId,
            @Payload Map<String, Object> payload
    ) {
        Long senderId = payload.get("senderId") != null
                ? Long.valueOf(payload.get("senderId").toString())
                : null;
        String text = payload.get("text").toString();

        ChatMessage savedMessage = messengerService.sendMessage(conversationId, senderId, text);

        messagingTemplate.convertAndSend("/topic/chat/" + conversationId, savedMessage);
    }
    @MessageMapping("/chat/{conversationId}/read")
    public void markAsRead(
            @DestinationVariable Long conversationId,
            @Payload Map<String, Object> payload
    ) {
        Long readerId = Long.valueOf(payload.get("readerId").toString());
        messengerService.markMessagesAsRead(conversationId, readerId);

        Object readEvent = Map.of("conversationId", conversationId, "readerId", readerId);
        messagingTemplate.convertAndSend("/topic/chat/" + conversationId + "/read", readEvent);
    }
}