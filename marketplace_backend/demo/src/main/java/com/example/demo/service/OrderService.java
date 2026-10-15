package com.example.demo.service;
import org.springframework.stereotype.Service;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.Orders;
import com.example.demo.entity.OrderItem;
import com.example.demo.repository.ProductRepository;

import jakarta.transaction.Transactional;

import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrdersRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;


@Service 
public class OrderService
{
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public Orders createOrder(Long userId)
    {
        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        if(cartItems.isEmpty())
        {
            throw new RuntimeException("Корзина пуста, невозможно оформить заказ!");
        }
        BigDecimal totalSum = BigDecimal.ZERO;
        for (CartItem item : cartItems)
        {
            Product product = item.getProduct();
            if(product.getQuantity() < item.getQuantity())
            {
                throw new RuntimeException("Ошибка склада");
            }
            int newQuntity = product.getQuantity() - item.getQuantity();
            product.setQuantity(newQuntity);
            productRepository.save(product);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            
            totalSum = totalSum.add(itemTotal);
        }
        Orders order = new Orders();
        order.setUserId(userId);
        order.setTotalPrice(totalSum);
        order.setStatus("В процессе");
        order.setCreatedAt(java.time.LocalDateTime.now());

        Orders saveOrder = ordersRepository.save(order);
        for(CartItem item : cartItems)
        {
            Product product = item.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(saveOrder);
            orderItem.setProduct(product);
            orderItem.setVendorId(product.getVendorId());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setStatus("В процессе");

            orderItemRepository.save(orderItem);    
        }
        cartRepository.deleteAll(cartItems);
        return saveOrder;
        
 
    }
}
