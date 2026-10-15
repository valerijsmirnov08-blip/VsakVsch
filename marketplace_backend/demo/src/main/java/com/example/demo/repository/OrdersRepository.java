package com.example.demo.repository;
import com.example.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository 
public interface OrdersRepository extends JpaRepository<Orders, Long>
{
    List<Orders> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Orders> findByUserId(Long userId);
}