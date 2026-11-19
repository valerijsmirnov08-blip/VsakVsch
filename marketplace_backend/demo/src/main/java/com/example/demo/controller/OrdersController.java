package com.example.demo.controller;

import com.example.demo.repository.OrdersRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.service.FinancesService;
import com.example.demo.service.OrderService;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Orders;
import com.example.demo.dto.ChartStatDTO;
import com.example.demo.dto.OrdersVendorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/orders")

@CrossOrigin(origins = "*")

public class OrdersController
{
    @Autowired 
    private OrderService orderService;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired 
    private ProductRepository productRepository;
    @Autowired
    private FinancesService financesService;

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
    @GetMapping ("/vendor/{vendorId}/total-sales")
    public ResponseEntity<?> getTotalSelsVendor(@PathVariable Long vendorId)
    {
        try
        {
            Integer total = orderItemRepository.countTotalOrdersByVendor(vendorId);
            return ResponseEntity.ok(total != null ? total: 0);
        }
        catch(Exception a)
        {
            return ResponseEntity.badRequest().body(a.getMessage());
        }
    }

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
    public ResponseEntity<?> updateStatus(@PathVariable Long itemId, @RequestBody  String status)
    {
        try
        {
            OrderItem  item = orderItemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Позиция заказа не найдена"));
            item.setStatus(status); 
            orderItemRepository.save(item);
            financesService.updateSaleStatusByOrderItem(itemId, status);
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
    @GetMapping("/vendor/{vendorId}/chart")
    public ResponseEntity<?> getVendorChart(@PathVariable Long vendorId, @RequestParam String period)
    {
        try
        {
            java.time.LocalDateTime startDate = java.time.LocalDateTime.now();
            List<ChartStatDTO> chartData;

            switch (period)
            {
                case "Неделя":
                    startDate = startDate.minusDays(7);
                    chartData = orderItemRepository.getChartByDay(vendorId, startDate);
                    break;
                case "2 недели":
                    startDate = startDate.minusDays(14);
                    chartData = orderItemRepository.getChartByDay(vendorId, startDate);
                    break;
                case "Месяц":
                    startDate = startDate.minusMonths(1);
                    chartData = orderItemRepository.getChartByDay(vendorId, startDate);
                    break;
                case "Год":
                    startDate = startDate.minusYears(1);
                    chartData = orderItemRepository.getChartByMonth(vendorId, startDate);
                    break;
                default:
                    startDate = startDate.minusMonths(1);
                    chartData = orderItemRepository.getChartByDay(vendorId, startDate);
                    break;
            }
            return ResponseEntity.ok(chartData);
        }  
        catch(Exception a)
        {
            a.printStackTrace();
            return ResponseEntity.badRequest().body(a.getMessage());
        }
    }
    @GetMapping ("/vendor/{vendorId}/summary-stats")
    public ResponseEntity<?> getVendorStats(@PathVariable Long vendorId)
    {
        try
        {
            Long totalOrders = orderItemRepository.countAllByVendorId(vendorId);
            Long canceledOrders = orderItemRepository.countCanceledByVendorId(vendorId);
            Long totalReviews = orderItemRepository.countReviewsByVendorId(vendorId);
            Double avgRating = productRepository.getAverageRatingByVendorId(vendorId);
            if(avgRating == null)
            {
                avgRating = 0.0;
            }
            int canceledPercent = totalOrders == 0 ? 0 : (int) ((canceledOrders * 100) / totalOrders);
            Map<String, Object> stats = new HashMap<>();
            stats.put("onTimeDelivery", 96);
            stats.put("deliveryRating", 98);
            stats.put("productRating", Math.round(avgRating * 10.0) / 10.0);
            stats.put("canceledPercent", canceledPercent);
            stats.put("totalReviews", totalReviews);

            return ResponseEntity.ok(stats);
        }
        catch(Exception a)
        {
            a.printStackTrace();
            return ResponseEntity.badRequest().body(a.getMessage());
        }
    }
}