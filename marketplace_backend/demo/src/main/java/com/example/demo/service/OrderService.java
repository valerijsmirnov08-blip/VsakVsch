package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.OrdersVendorDTO;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.Orders;
import com.example.demo.entity.OrderItem;

import jakarta.transaction.Transactional;

import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrdersRepository;
import com.example.demo.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import java.util.List;

import java.math.BigDecimal;


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
    @Autowired
    private FinancesService financesService;
    @Autowired 
    private AnalyticsService analyticsService;

     public Page<OrdersVendorDTO> getOrdersPage(Long vendorId, int page, int size)
    {
        return orderItemRepository.findByVendorId(vendorId, PageRequest.of(page, size))
        .map(order -> new OrdersVendorDTO(order));
    }

    @Transactional
    public Orders createOrder(Long userId)
    {
        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        if(cartItems.isEmpty())
        {
            throw new RuntimeException("Корзина пуста, невозможно оформить заказ!");
        }
        BigDecimal totalSum = BigDecimal.ZERO;
        for(CartItem item : cartItems)
        {
            Product product = item.getProduct();
            if(product.getQuantity() < item.getQuantity())
            {
                throw new RuntimeException("Ошибка склада");
            }
            int newQuantity = product.getQuantity() - item.getQuantity();
            product.setQuantity(newQuantity);
            if (product.getRating() == null)
            {
                product.setRating(0.0);
            }
            productRepository.save(product);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalSum = totalSum.add(itemTotal);
        }
        Orders order = new Orders();
        order.setUserId(userId);
        order.setTotalPrice(totalSum);
        order.setStatus("В процессе");
        order.setCreatedAt(java.time.LocalDateTime.now());
        Orders saveOreder = ordersRepository.save(order);

        for(CartItem item: cartItems)
        {
            Product product = item.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(saveOreder);
            orderItem.setProduct(product);
            orderItem.setVendorId(product.getVendor().getId());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setStatus("В процессе");

            OrderItem savedItem = orderItemRepository.save(orderItem);
            financesService.recordSale(savedItem);
            analyticsService.recordProductSale(product.getId(), item.getQuantity());
        }
        cartRepository.deleteAll(cartItems);
        return saveOreder;
    }
    }