package com.example.demo.repository;

import com.example.demo.entity.Favorite;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface FavoriteRepository extends JpaRepository<Favorite, Long>
{
    Optional<Favorite> findByUserIdAndProductId(Long userId, Long productId);
    Page<Favorite> findByUserId(Long userId, Pageable pageable);

    void deleteByUserIdAndProductId(Long userId, Long productId);


    
}