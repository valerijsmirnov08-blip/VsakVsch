package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.FavoriteRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UsersRepository;

import jakarta.transaction.Transactional;

@RestController 
@RequestMapping ("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173")
public class FavoriteController
{
    @Autowired 
    FavoriteRepository favoriteRepository;
    @Autowired 
    UsersRepository usersRepository;
    @Autowired 
    ProductRepository productRepository;

    public static class InnerFavoriteController
    {
        private Long userId;
        private Long productId;

        public InnerFavoriteController() {}

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        
    }
    @PostMapping ("/toggle")
    @Transactional
    public ResponseEntity<?> toggleFavorite(@RequestBody InnerFavoriteController request)
    {
        var existing = favoriteRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId());
        if(existing.isPresent())
        {
            favoriteRepository.deleteByUserIdAndProductId(request.getUserId(), request.getProductId());
        }
        else
        {
            var user = usersRepository.findById(request.getUserId()).orElseThrow();
            var product = productRepository.findById(request.getProductId()).orElseThrow();
            favoriteRepository.save(new com.example.demo.entity.Favorite(user, product));
        }
        return ResponseEntity.ok().build();
    }
    @GetMapping ("/check/{userId}/{productId}")
    public  ResponseEntity<Boolean> chekFavorite(@PathVariable  Long userId, @PathVariable  Long productId)
    {
        boolean exists = favoriteRepository.findByUserIdAndProductId(userId, productId).isPresent();
        return ResponseEntity.ok(exists);
    }
    @GetMapping ("/user/{userId}")
    public ResponseEntity<?> getUserFavorite(@PathVariable Long userId)
    {
        var userFavorites =    favoriteRepository.findAll().stream()
        .filter(a -> a.getUser().getId().equals(userId))
        .map(a -> a.getProduct())
        .toList();

        return  ResponseEntity.ok(userFavorites);
    }
}
