package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ProductMonthlyStat;

@Repository 
public interface ProductMonthlyStatRepository extends JpaRepository<ProductMonthlyStat, Long>
{
   Optional<ProductMonthlyStat> findByProductIdAndMonthLabel(Long productId, String monthLabel);
}
