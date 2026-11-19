package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.FinanceSummaryDto;
import com.example.demo.entity.Finances;
import com.example.demo.service.FinancesService;
import java.util.List;

@RestController 
@RequestMapping("/api/finances")
@CrossOrigin(origins = "*")
public class FinancesController
{
    @Autowired 
    private FinancesService financesService;

    @GetMapping("/summary/{vendorId}")
    public ResponseEntity<FinanceSummaryDto> getBalance(@PathVariable Long vendorId)
    {
        return ResponseEntity.ok(financesService.getVendorBalanceSummary(vendorId));
    }
    @GetMapping("/payouts/{vendorId}")
    public ResponseEntity<List<Finances>> getPayouts(@PathVariable Long vendorId)
    {
        return ResponseEntity.ok(financesService.getVendorPayouts(vendorId));
    } 
    @PostMapping ("/payout/{vendorId}")
    public ResponseEntity<?> createPayout(@PathVariable Long vendorId)
    {
        try
        {   
            return ResponseEntity.ok(financesService.requestPayout(vendorId));
        }
        catch(RuntimeException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
