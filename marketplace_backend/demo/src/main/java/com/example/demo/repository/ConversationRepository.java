package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Conversation;

public interface ConversationRepository extends JpaRepository<Conversation, Long>
{
    Optional<Conversation> findByBuyerIdAndVendorIdAndProductId(Long buyerId, Long vendorId, Long productId);

    List<Conversation> findByBuyerIdOrVendorIdOrderByUpdatedAtDesc(Long buyerId, Long vendorId);

    Optional<Conversation> findByBuyerIdAndType(Long buyerId, String type);
    List<Conversation> findByProductId(Long productId);
}
