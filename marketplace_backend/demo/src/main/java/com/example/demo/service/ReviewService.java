package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductMonthlyStat;
import com.example.demo.entity.Review;
import com.example.demo.repository.ProductMonthlyStatRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;


@Service 
public class ReviewService
{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired 
    private ProductMonthlyStatRepository productMonthlyStatRepository;



    public List<Review> getReviewsProducts(Long productId)
    {
        return reviewRepository.findByProductId(productId);
    }
    @Transactional 
    public Review addReview(Long productId,Long userId, int rating, String comment)
    {
        Review review = new Review();
        review.setProductId(productId);
        review.setUserId(userId);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        Double avg = reviewRepository.findAverageRatingByProductId(productId);

        Product product = productRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Товар не найден"));

        if(avg != null)
        {
            double roundedRating = BigDecimal.valueOf(avg)
                    .setScale(1, RoundingMode.HALF_UP)
                    .doubleValue();
            product.setRating(roundedRating);
        }
        else
        {
            product.setRating(0.0);
        }
        productRepository.save(product);


        String currentMonth = java.time.YearMonth.now().toString();
        int updatedReviews =  productMonthlyStatRepository.incrementReviewsCount(product.getId(), currentMonth);
        if(updatedReviews == 0)
        {
            ProductMonthlyStat newStat  = new ProductMonthlyStat();
            newStat.setReviewsCount(1);
            newStat.setSalesCount(0);
            newStat.setProductId(product.getId());
            newStat.setMonthLabel(currentMonth); 

            productMonthlyStatRepository.save(newStat);
        }

        return savedReview;
    }
}
