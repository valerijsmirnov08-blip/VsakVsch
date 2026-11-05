package com.example.demo.controller;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.ComplaintCategory;
import com.example.demo.entity.ComplaintStatus;
import com.example.demo.repository.ComplaintCategoryRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired 
    private ReviewRepository reviewRepository;
    @Autowired
    private ComplaintCategoryRepository complaintCategoryRepository;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProduct(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewsProducts(productId);
        return ResponseEntity.ok(reviews);
    }
    @PostMapping
        public ResponseEntity<Void> addReview(@RequestBody ReviewRequest request) {
        reviewService.addReview(
                request.getProductId(),
                request.getUserId(),
                request.getRating(),
                request.getComment(),
                request.getOrderItemId(),
                request.getPhotoUrl(),
                request.getIsComplaint(),
                request.getComplaintCategoryIds(),
                request.getComplaintText()
        );
        return ResponseEntity.ok().build();
    }
    @GetMapping ("/vendor/{vendorId}")
    public ResponseEntity<?> getVendorReviews(@PathVariable Long vendorId) {
        try {
            List<Review> reviews = reviewRepository.findByVendorId(vendorId);
            List<Map<String, Object>> result = new ArrayList<>();
            
            for (Review r : reviews) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", r.getId());
                map.put("productId", r.getProductId());
                map.put("rating", r.getRating());
                map.put("comment", r.getComment());
                map.put("isRead", r.getIsRead());
                map.put("isComplaint", r.getIsComplaint());
                map.put("photoUrls", java.util.Collections.emptyList());
                map.put("complaintStatus", r.getComplaintStatus());
                map.put("createdAt", r.getCreatedAt());


                Set<String> parentNames = new java.util.HashSet<>();
                List<String> subNames = new java.util.ArrayList<>();

                for (ComplaintCategory cat : r.getComplaintCategories())
                {
                    subNames.add(cat.getName());
                    if (cat.getParentId() != null)
                    {
                        Optional<ComplaintCategory> parent = complaintCategoryRepository.findById(cat.getParentId());
                        if (parent.isPresent())
                        {
                            parentNames.add(parent.get().getName());
                        }
                    }
                }
                map.put("parentCategories", String.join(", ", parentNames));
                map.put("subCategories", String.join(", ", subNames));
                
                result.add(map);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ошибка на сервере: " + e.toString());
        }
    }
    @PutMapping("/update-status")
    public ResponseEntity<?> updateReview(@RequestBody Map<String, Object> payload)
    {
        try
        {
            Long reviewId = Long.valueOf(payload.get("id").toString());
            System.out.println("Пытаемся найти и обновить отзыв с ID: " + reviewId);
            Boolean newStatus = (Boolean) payload.get("isRead");
            Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
            if(reviewOpt.isPresent())
            {
                Review r = reviewOpt.get();
                r.setIsRead(newStatus);
                reviewRepository.save(r);
                Map<String, String> response = new java.util.HashMap<>();
                response.put("message", "Статус успешно обновлен");
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(404).body("Отзыв не найден в БД");
        }catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ошибка сервера: " + e.getMessage());
        }
    }
    @PutMapping ("/update-complaint-status")
    public ResponseEntity<?> updateStatus(@RequestBody Map<String, Object> payload)
    {
        try
        {
                Long reviewId = Long.valueOf(payload.get("id").toString());
                String statusStr = (String) payload.get("status");
                ComplaintStatus newStatus = ComplaintStatus.valueOf(statusStr);
                Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
                if (reviewOpt.isPresent())
                {
                    Review a = reviewOpt.get();
                    a.setComplaintStatus(newStatus);
                    reviewRepository.save(a);
                    Map<String, String> response = new java.util.HashMap<>();
                    response.put("message", "Статус жалобы успешно обновлен");
                    return ResponseEntity.ok(response);
                }
                return ResponseEntity.status(404).body("Отзыв не найден в БД");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ошибка сервера: " + e.getMessage());
        }
        }
}
    
        