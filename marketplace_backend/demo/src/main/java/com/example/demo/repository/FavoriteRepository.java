package com.example.demo.repository;

import com.example.demo.entity.Favorite;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository 
public interface FavoriteRepository extends JpaRepository<Favorite, Long>
{
    Optional<Favorite> findByUserIdAndProductId(Long userId, Long productId);

    void deleteByUserIdAndProductId(Long userId, Long productId);


    
}