package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // Классический плоский DTO класс с пустым конструктором и геттерами/сеттерами
    public static class CartRequest {
        private Long userId;
        private Long productId;
        private int quantity;

        public CartRequest() {}

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartRequest request) {
        // Логируем в консоль Spring Boot, чтобы увидеть, что прислал фронтенд
        System.out.println("=== ПОЛУЧЕН ЗАПРОС В КОРЗИНУ ===");
        System.out.println("userId: " + request.getUserId());
        System.out.println("productId: " + request.getProductId());
        System.out.println("=================================");

        if (request.getUserId() == null || request.getProductId() == null) {
            return ResponseEntity.badRequest().body("Ошибка: userId или productId пустые в запросе!");
        }

        Product product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body("Товар с таким ID не найден в базе данных");
        }

        CartItem cartItem = new CartItem(request.getUserId(), product, request.getQuantity());
        CartItem savedItem = cartRepository.save(cartItem);
        
        return ResponseEntity.ok(savedItem);
    }

    @GetMapping("/{userId}")
    public List<CartItem> getCartByUserId(@PathVariable Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteFromCart(@PathVariable Long id) {
        cartRepository.deleteById(id);
    }
}