package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.OrdersVendorDTO;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.Orders;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.ProductMonthlyStat;

import jakarta.transaction.Transactional;

import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrdersRepository;
import com.example.demo.repository.ProductMonthlyStatRepository;
import com.example.demo.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import java.util.List;

import java.math.BigDecimal;
// import java.util.Optional;


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
    private ProductMonthlyStatRepository productMonthlyStatRepository;

     public Page<OrdersVendorDTO> getOrdersPage(Long vendorId, int page, int size)
    {
        return orderItemRepository.findByVendorId(vendorId, PageRequest.of(page, size))
        .map(order -> new OrdersVendorDTO(order));
    }

    @Transactional
    public Orders createOrder(Long userId)
    {
        // public List<OrdersVendorDTO> getAllOrders()
        // {
        //     return orderItemRepository.findAll().stream()
        //     .map(order -> new OrdersVendorDTO(order))
        //     .toList();
        // }
       



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
        String currentMonth = java.time.YearMonth.now().toString();
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

            // Optional<ProductMonthlyStat> productMonthlyStats = productMonthlyStatRepository.findByProductIdAndMonthLabel(product.getId(), currentMonth);
            // if(productMonthlyStats.isPresent())
            // {
            //     ProductMonthlyStat stat = productMonthlyStats.get();

            //     Integer a =  stat.getSalesCount() + item.getQuantity();

            //     stat.setSalesCount(a);

            //     productMonthlyStatRepository.save(stat);
            // }
            // else
            // {
            //     ProductMonthlyStat newStat = new ProductMonthlyStat();
            //     newStat.setProductId(product.getId());
            //     newStat.setMonthLabel(currentMonth);
            //     newStat.setSalesCount(item.getQuantity());

            //     productMonthlyStatRepository.save(newStat);
            // }

            int updatedRows =  productMonthlyStatRepository.incrementSalesCount(product.getId(), currentMonth, item.getQuantity());
            if(updatedRows == 0)
            {
                ProductMonthlyStat newStat = new ProductMonthlyStat();
                newStat.setProductId(product.getId());
                newStat.setMonthLabel(currentMonth);
                newStat.setSalesCount(item.getQuantity()); 

                productMonthlyStatRepository.save(newStat);
            }
        }
            
            cartRepository.deleteAll(cartItems);
            return saveOrder;
        }
        
        
       
 
    }