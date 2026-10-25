package com.example.demo.controller;

import com.example.demo.entity.ProductMonthlyStat;
import com.example.demo.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "http://localhost:5173")
public class AnalyticsController
{
    @Autowired
    AnalyticsService analyticsService;

    @GetMapping ("/product/{productId}")
    public ResponseEntity <List<ProductMonthlyStat>> findByProductIdOrderByMonthLabelAsc(@PathVariable Long productId)
    {
        List<ProductMonthlyStat> analitics = analyticsService.getProductStats(productId);
        return ResponseEntity.ok(analitics);
    }
}
