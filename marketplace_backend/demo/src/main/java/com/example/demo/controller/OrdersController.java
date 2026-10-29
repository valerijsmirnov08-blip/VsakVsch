package com.example.demo.controller;

import com.example.demo.repository.OrdersRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.service.OrderService;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Orders;
import com.example.demo.dto.OrdersVendorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.util.List;

@RestController
@RequestMapping("/api/orders")

@CrossOrigin(origins = "http://localhost:5173")

public class OrdersController
{
    @Autowired 
    private OrderService orderService;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<?> createOrder(@PathVariable  Long userId)
    {
        try
        {
            Orders order = orderService.createOrder(userId);
            return ResponseEntity.ok(order);
        }
        catch(RuntimeException a)
        {
            return ResponseEntity.badRequest().body(a.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserOrders(@PathVariable Long userId)
    {
        try
        {
            List<Orders> orders = ordersRepository.findByUserIdOrderByCreatedAtDesc(userId);
            return ResponseEntity.ok(orders);
        }
        catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping ("/vendor/{vendorId}")
    public Page<OrdersVendorDTO> getOrdersPage
    (
        @RequestParam (defaultValue = "0") int page,
        @RequestParam (defaultValue = "20") int size,
        @PathVariable Long vendorId
    )
    {
        return orderService.getOrdersPage(vendorId, page, size);
    }
    // @GetMapping("/vendor/{vendorId}")
    // public ResponseEntity<?> findVendorId(@PathVariable Long vendorId)
    // {
    //     try
    //     {
    //         List<OrderItem> order = orderItemRepository.findByVendorId(vendorId);
    //         return ResponseEntity.ok(order);
    //     }
    //     catch(RuntimeException e)
    //     {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }
    @GetMapping("/user/{userId}/items")
    public ResponseEntity<?> getUserOrderItems(@PathVariable Long userId)
    {
        try
        {
            List<OrderItem> items = orderItemRepository.findByOrderUserId(userId);
            return ResponseEntity.ok(items);
        }
        catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/items/{itemId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long itemId, @RequestParam String status)
    {
        try
        {
            OrderItem  item = orderItemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Позиция заказа не найдена"));
            item.setStatus(status); 
            orderItemRepository.save(item);
            return ResponseEntity.ok(item);
        }
        catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping ("/items/{itemId}/reviewed")
    public ResponseEntity<?> updateReviwed(@PathVariable Long itemId)
    {
        try
        {
            OrderItem item = orderItemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Позиция заказа не найдена"));
            item.setIsReviewed(true);
            orderItemRepository.save(item);

            return ResponseEntity.ok(item);
        }
        catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}