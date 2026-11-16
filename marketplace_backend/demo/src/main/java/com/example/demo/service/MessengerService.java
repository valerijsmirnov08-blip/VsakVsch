package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.ChatMessage;
import com.example.demo.entity.Conversation;
import com.example.demo.entity.Role;
import com.example.demo.entity.Users;

import org.springframework.stereotype.Service;

import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.repository.ConversationRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UsersRepository;

@Service 
public class MessengerService
{
    @Autowired 
    private ConversationRepository conversationRepository;
    @Autowired 
    private ChatMessageRepository chatMessageRepository;
    @Autowired 
    private UsersRepository usersRepository;
    @Autowired 
    private ProductRepository productRepository;

    public Conversation openProductChat(Long buyerId, Long vendorId, Long productId)
    {
        return conversationRepository.findByBuyerIdAndVendorIdAndProductId(buyerId, vendorId, productId)
        .orElseGet(() -> createNewProductChat(buyerId, vendorId, productId));
    }
    private Conversation createNewProductChat(Long buyerId, Long vendorId, Long productId)
    {
        Conversation chat = new Conversation();
        chat.setBuyer(usersRepository.findById(buyerId).orElseThrow());
        chat.setVendor(usersRepository.findById(vendorId).orElseThrow());
        chat.setProduct(productRepository.findById(productId).orElseThrow());
        return conversationRepository.save(chat);
    }
    public Conversation openSupportChat(Long buyerId, String guestEmail)
    {
        if(buyerId != null)
        {
            return conversationRepository.findByBuyerIdAndType(buyerId, "SUPPORT")
            .orElseGet(() -> createSupportChat(buyerId, guestEmail));
        }
        return createSupportChat(null,guestEmail);
    }
    private Conversation createSupportChat(Long buyerId, String guestEmail)
    {
        Conversation chat = new Conversation();
        if(buyerId != null)
        chat.setBuyer(usersRepository.findById(buyerId).orElseThrow());
        chat.setVendor(usersRepository.findFirstByRole(Role.ADMIN).orElseThrow());
        chat.setType("SUPPORT");
        chat.setGuestEmail(guestEmail);
        return conversationRepository.save(chat);
    }
    public ChatMessage sendMessage(Long conversationId, Long senderId, String text)
    {
        Conversation chat = conversationRepository.findById(conversationId).orElseThrow();
        ChatMessage msg = new ChatMessage();
        msg.setConversation(chat);
        msg.setText(text);
        if(senderId != null)
        {
            Users sender = usersRepository.findById(senderId).orElseThrow();
            msg.setSenderUser(sender);
            msg.setSender(sender.getUserName());
        }
        else
        {
            msg.setSender("GUEST");
        }
        chat.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(chat);
        return chatMessageRepository.save(msg);
    }
    public List<Conversation> getUserConversations(Long userId)
    {
        return conversationRepository.findByBuyerIdOrVendorIdOrderByUpdatedAtDesc(userId, userId);
    }
    public List<ChatMessage> getChatMessages(Long conversationId, Long readerId)
    {
        List<ChatMessage> messages = chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        markIncomingAsRead(messages, readerId);
        return messages;
    }
    private void markIncomingAsRead(List<ChatMessage> messages, Long readerId)
    {
        if(readerId == null)
        {
            return;
        }
        for(ChatMessage msg : messages)
        {
            boolean isFromOther = msg.getSenderUser() == null || !msg.getSenderUser().getId().equals(readerId);
            if(!Boolean.TRUE.equals(msg.getIsRead()) && isFromOther)
            {
                msg.setIsRead(true);
                chatMessageRepository.save(msg);
            }
        }
    }
}
