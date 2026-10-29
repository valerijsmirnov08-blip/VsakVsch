package com.example.demo.repository;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Orders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository 
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>
{
    public List<Orders> findByVendorIdAndStatus(Long vendorId, String status);
    Page<OrderItem> findByVendorId(Long vendorId, Pageable   pageable);
    List<OrderItem> findByOrderUserId(Long userId);
}
