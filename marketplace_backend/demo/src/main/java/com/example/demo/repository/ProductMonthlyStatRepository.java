package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import com.example.demo.entity.ProductMonthlyStat;

@Repository 
public interface ProductMonthlyStatRepository extends JpaRepository<ProductMonthlyStat, Long>
{
   Optional<ProductMonthlyStat> findByProductIdAndStatDate(Long productId, LocalDate statDate);
   @Modifying
   @Query("UPDATE ProductMonthlyStat p SET p.salesCount = p.salesCount + :quantity WHERE p.productId = :productId AND p.statDate = :statDate")
   int incrementSalesCount(@Param ("productId") Long productId, @Param ("statDate") LocalDate statDate, @Param ("quantity")  Integer quantity);

   @Modifying 
   @Query("UPDATE ProductMonthlyStat a SET a.reviewsCount = a.reviewsCount + 1 WHERE a.productId = :productId AND a.statDate = :statDate")
   int incrementReviewsCount(@Param("productId") Long productId, @Param("statDate") LocalDate statDate);
   
   @Query("SELECT s FROM ProductMonthlyStat s WHERE s.statDate >= :startDate AND s.productId IN (SELECT p.id FROM Product p WHERE p.vendorId = :vendorId)") 
   List<ProductMonthlyStat> findStatsByVendorAndDate(@Param ("vendorId") Long vendorId, @Param("startDate") LocalDate startDate );

   List<ProductMonthlyStat> findByProductIdOrderByStatDateAsc(Long productId);
}
