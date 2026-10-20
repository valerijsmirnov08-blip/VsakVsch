package com.example.demo.controller;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.entity.Review;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProduct(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewsProducts(productId);
        return ResponseEntity.ok(reviews);
    }
    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody ReviewRequest request) {
        Review createdReview = reviewService.addReview(
                request.getProductId(),
                request.getUserId(),
                request.getRating(),
                request.getComment()
        );
        return ResponseEntity.ok(createdReview);
    }
}