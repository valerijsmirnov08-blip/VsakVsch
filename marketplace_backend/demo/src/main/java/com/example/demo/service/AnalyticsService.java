package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.example.demo.entity.ProductMonthlyStat;
import com.example.demo.repository.ProductMonthlyStatRepository;
import java.util.List;

@Service 

@Transactional 
public class AnalyticsService
{
    @Autowired 
    private ProductMonthlyStatRepository productMonthlyStatRepository;

    public List<ProductMonthlyStat> getProductStats(Long productId)
    {
        return productMonthlyStatRepository.findByProductIdOrderByMonthLabelAsc(productId);
    }
}
