package com.example.demo.repository;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{
    List<Product> findByVendor_Id(Long vendorId);
    List<Product> findByCategory_Id(Long categoryId);

    @Query(value = "SELECT * FROM products WHERE " +
        "(:categoryId IS NULL OR category_id = :categoryId) AND " +
        "(brand ILIKE CONCAT('%', :searchText, '%') OR " +
        " model ILIKE CONCAT('%', :searchText, '%') OR " +
        " vendor_sku ILIKE CONCAT('%', :searchText, '%') OR " +
        " similarity(brand, :searchText) > 0.2 OR " +
        " similarity(model, :searchText) > 0.2)", 
        nativeQuery = true)
    List<Product> searchProducts(@Param ("searchText") String searchText, @Param("categoryId") Long categoryId);

    @Query("SELECT AVG(p.rating) FROM Product p WHERE p.vendor.id = :vendorId AND p.rating > 0")
    Double getAverageRatingByVendorId(@Param("vendorId") Long vendorId);
}