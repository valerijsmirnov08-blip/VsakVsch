package com.example.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Finances;
import com.example.demo.entity.FinanceStatus;
import com.example.demo.entity.FinanceType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository 
public interface FinancesRepository extends JpaRepository<Finances, Long>
{
    List<Finances> findByVendorVendorIdAndTypeOrderByCreatedAtDesc(Long vendorId, FinanceType type);

    @Query("SELECT COALESCE(SUM(f.netAmount), 0) FROM Finances f " +
        "WHERE f.vendor.vendor.id = :vendorId AND f.type = :type AND f.status = :status")
    BigDecimal sumNetAmountByVendorAndTypeAndStatus
    (
            @Param("vendorId") Long vendorId,
            @Param("type") FinanceType type,
            @Param("status") FinanceStatus status
    );

    Optional<Finances> findByOrderItemId(Long orderItemId);
}
