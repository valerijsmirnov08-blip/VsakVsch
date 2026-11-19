package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.VendorRatingDTO;
import com.example.demo.service.VendorRatingService;

@RestController 
@RequestMapping ("api/vendor-rating")
@CrossOrigin(origins = "*")
public class VendorRatingController
{
    @Autowired 
    private VendorRatingService vendorRatingService;

    @GetMapping ("/{vendorId}")
    public ResponseEntity<VendorRatingDTO> getRating(@PathVariable Long vendorId, @RequestParam String period)
    {
        return ResponseEntity.ok(vendorRatingService.getVendorRatingDTO(vendorId, period));
    }
}
