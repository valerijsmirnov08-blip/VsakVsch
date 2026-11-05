package com.example.demo.controller;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;
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
    @Autowired 
    private ReviewRepository reviewRepository;

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
                request.getComplaintCategoryIds()
        );
        return ResponseEntity.ok().build();
    }
    @GetMapping ("/vendor/{vendorId}")
    public ResponseEntity<?> getVendorReviews(@PathVariable Long vendorId) {
        try {
            List<Review> reviews = reviewRepository.findByVendorId(vendorId);
            List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
            
            for (Review r : reviews) {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                map.put("id", r.getId());
                map.put("productId", r.getProductId());
                map.put("rating", r.getRating());
                map.put("comment", r.getComment());
                map.put("isRead", r.getIsRead());
                map.put("photoUrls", java.util.Collections.emptyList());
                result.add(map);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ошибка на сервере: " + e.toString());
        }
    }
    // @PutMapping
    // @PutMapping ("/{id}")
    // public ResponseEntity<?> updateProductById(@PathVariable Long id, @RequestBody Product updatedData)
    // {
    //     if(!productRepository.existsById(id))
    //     {
    //         return ResponseEntity.notFound().build();
    //     }
    //     Optional<Product> productOpt = productRepository.findById(id);
    //     Product exisitingProduct = productOpt.get();
    //     exisitingProduct.setBrand(updatedData.getBrand());
    //     exisitingProduct.setModel(updatedData.getModel());
    //     exisitingProduct.setQuantity(updatedData.getQuantity());
    //     exisitingProduct.setDescription(updatedData.getDescription());
    //     exisitingProduct.setWarranty(updatedData.getWarranty());
    //     exisitingProduct.setEstimatedDelivery(updatedData.getEstimatedDelivery());
    //     exisitingProduct.setPrice(updatedData.getPrice()); 
    //     productRepository.save(exisitingProduct);
    //     return  ResponseEntity.ok("Товар успешно обнавлён");
    // }
}