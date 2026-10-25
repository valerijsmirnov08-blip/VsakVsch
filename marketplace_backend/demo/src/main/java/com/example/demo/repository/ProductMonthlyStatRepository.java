package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.example.demo.entity.ProductMonthlyStat;

@Repository 
public interface ProductMonthlyStatRepository extends JpaRepository<ProductMonthlyStat, Long>
{
   Optional<ProductMonthlyStat> findByProductIdAndMonthLabel(Long productId, String monthLabel);
   @Modifying
   @Query("UPDATE ProductMonthlyStat p SET p.salesCount = p.salesCount + :quantity WHERE p.productId = :productId AND p.monthLabel = :monthLabel")
   int incrementSalesCount(@Param ("productId") Long productId, @Param ("monthLabel") String monthLabel, @Param ("quantity")  Integer quantity);

   @Modifying 
   @Query("UPDATE ProductMonthlyStat a SET a.reviewsCount = a.reviewsCount + 1 WHERE a.productId = :productId AND a.monthLabel = :monthLabel")
   int incrementReviewsCount(@Param("productId") Long productId, @Param("monthLabel") String monthLabel);

   List<ProductMonthlyStat> findByProductIdOrderByMonthLabelAsc(Long productId);
}
