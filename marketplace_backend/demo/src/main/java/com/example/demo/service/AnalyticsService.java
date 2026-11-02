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
        return productMonthlyStatRepository.findByProductIdOrderByStatDateAsc(productId);
    }
    
    public List<ProductMonthlyStat> getVendorStats(Long vendorId, String period)
    {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate startDate;
        switch (period.toLowerCase()) {
            case "week":
                startDate = today.minusWeeks(1);
                break;
             case "2_weeks":
                startDate = today.minusWeeks(2);
                break;
             case "month":
                startDate = today.minusMonths(1);
                break;
             case "halfyear":
                startDate = today.minusMonths(6);
                break;
             case "year":
                startDate = today.minusYears(1);
                break;
            default:
                startDate = today.minusYears(1000);
                break;
        }
        return productMonthlyStatRepository.findStatsByVendorAndDate(vendorId, startDate);
    }
}
