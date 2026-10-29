package com.example.demo.repository;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
// import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{
    List<Product> findByVendorId(Long vendorId);
    List<Product> findByCategory_Id(Long categoryId);
    // @Query(value = "SELECT * FROM products WHERE " +"(:categoryId IS NULL OR category_id = :categoryId) AND " + "MATCH(model, vendor_sku) AGAINST(:searchText IN BOOLEAN MODE)", nativeQuery = true)
    // List<Product> searchProducts(@Param("searchText") String searchText, @Param("categoryId") Long categoryId);
}