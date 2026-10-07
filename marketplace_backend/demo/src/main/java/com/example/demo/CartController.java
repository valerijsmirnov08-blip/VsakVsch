package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

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
        if (request.getUserId() == null || request.getProductId() == null)
        {
            return ResponseEntity.badRequest().body("Ошибка: userId или productId пустые в запросе!");
        }
        Optional<CartItem> existingItemOpt = cartRepository.findByUserIdAndProductId
        (
            request.getUserId(),
            request.getProductId());
        if (existingItemOpt.isPresent())
        {
            CartItem existingItem = existingItemOpt.get();
            int newQuntity = existingItem.getQuantity() + request.getQuantity();
            existingItem.setQuantity(newQuntity);
            CartItem updatedItem = cartRepository.save(existingItem);
            return ResponseEntity.ok(updatedItem);
        }
        Product product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body("Товар с таким ID не найден");
        }
        int qty = request.getQuantity() > 0 ? request.getQuantity() : 1;
        CartItem cartItem = new CartItem(request.getUserId(), product,qty);
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