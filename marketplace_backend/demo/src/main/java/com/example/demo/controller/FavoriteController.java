package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import com.example.demo.dto.FavoriteDTO;
import com.example.demo.repository.FavoriteRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.FavoriteService;

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
    @Autowired 
    FavoriteService favoriteService;

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

    @GetMapping ("/user/{userId}")
    public Page<FavoriteDTO> getUserFavorite
    (
        @RequestParam (defaultValue = "0") int page,
        @RequestParam (defaultValue = "20")int size,
        @PathVariable Long userId
    )
    {
        return favoriteService.getFavoritePage(userId, page, size);
    }
}
