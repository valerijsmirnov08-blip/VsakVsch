package com.example.demo.repository;
import com.example.demo.dto.ChartStatDTO;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Orders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository 
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>
{
    public List<Orders> findByVendorIdAndStatus(Long vendorId, String status);
    Page<OrderItem> findByVendorId(Long vendorId, Pageable   pageable);
    List<OrderItem> findByOrderUserId(Long userId);
    void deleteByProduct_Id(Long productId);

    @Query(value = "WITH date_series AS ( " +
           "  SELECT generate_series(CAST(:startDate AS DATE), CURRENT_DATE, '1 day'::interval)::date AS date_val " +
           ") " +
           "SELECT " +
           "  TO_CHAR(ds.date_val, 'DD.MM') AS label, " +
           "  COALESCE(CAST(SUM(oi.quantity) AS INTEGER), 0) AS value " +
           "FROM date_series ds " +
           "LEFT JOIN orders o ON CAST(o.created_at AS DATE) = ds.date_val " +
           "LEFT JOIN order_items oi ON o.id = oi.order_id AND oi.vendor_id = :vendorId " +
           "GROUP BY ds.date_val " +
           "ORDER BY ds.date_val ASC", 
           nativeQuery = true)
    List<ChartStatDTO> getChartByDay(@Param("vendorId") Long vendorId, @Param("startDate") java.time.LocalDateTime startDate);

    @Query(value = "WITH date_series AS ( " +
           "  SELECT generate_series(date_trunc('month', CAST(:startDate AS DATE)), date_trunc('month', CURRENT_DATE), '1 month'::interval)::date AS date_val " +
           ") " +
           "SELECT " +
           "  TO_CHAR(ds.date_val, 'MM.YYYY') AS label, " +
           "  COALESCE(CAST(SUM(oi.quantity) AS INTEGER), 0) AS value " +
           "FROM date_series ds " +
           "LEFT JOIN orders o ON date_trunc('month', CAST(o.created_at AS DATE)) = ds.date_val " +
           "LEFT JOIN order_items oi ON o.id = oi.order_id AND oi.vendor_id = :vendorId " +
           "GROUP BY ds.date_val " +
           "ORDER BY ds.date_val ASC", 
           nativeQuery = true)
    List<ChartStatDTO> getChartByMonth(@Param("vendorId") Long vendorId, @Param("startDate") java.time.LocalDateTime startDate);

    @Query("SELECT SUM(o.quantity) FROM OrderItem o WHERE o.vendorId = :vendorId")
    Integer countTotalOrdersByVendor(@Param("vendorId") Long vendorId);

    @Query("SELECT COUNT(o) FROM OrderItem o WHERE o.vendorId = :vendorId")
    Long countAllByVendorId(@Param("vendorId") Long vendorId);

    @Query("SELECT COUNT(o) FROM OrderItem o WHERE o.vendorId = :vendorId AND o.status = 'Отменен'")
    Long countCanceledByVendorId(@Param("vendorId") Long vendorId);

    @Query("SELECT COUNT(o) FROM OrderItem o WHERE o.vendorId = :vendorId AND o.isReviewed = true")
    Long countReviewsByVendorId(@Param("vendorId") Long vendorId);

    List<OrderItem> findByVendorId(Long vendorId);
}
