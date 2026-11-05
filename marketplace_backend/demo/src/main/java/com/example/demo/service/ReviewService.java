package com.example.demo.service;

import com.example.demo.entity.ComplaintCategory;
import com.example.demo.entity.ComplaintStatus;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductMonthlyStat;
import com.example.demo.entity.Review;
import com.example.demo.entity.ReviewPhoto;
import com.example.demo.repository.ComplaintCategoryRepository;
import com.example.demo.repository.OrderItemRepository;
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
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ComplaintCategoryRepository complaintCategoryRepository;



    public List<Review> getReviewsProducts(Long productId)
    {
        return reviewRepository.findByProductId(productId);
    }
    @Transactional 
    public Review addReview(Long productId,Long userId, int rating, String comment, Long orderItemId, List<String> photoUrl, boolean isComplaint, List<Long> complaintCategoryIds, String complaintText)
    {
        Review review = new Review();
        OrderItem item = orderItemRepository.findById(orderItemId).orElseThrow();
        if (photoUrl != null) {
            for (String url : photoUrl) {
                ReviewPhoto photo = new ReviewPhoto();
                photo.setUrl(url);                    
                photo.setReviewsId(review);       
                review.getPhotos().add(photo);
            }
        }
        review.setProductId(productId);
        review.setUserId(userId);
        review.setRating(rating);
        review.setComment(comment);
        review.setOrderItemId(item);
        review.setIsRead(false);
        review.setIsComplaint(isComplaint);
        review.setComplaintText(complaintText);
        review.setCreatedAt(LocalDateTime.now());
        if (isComplaint)
        {
            review.setComplaintStatus(ComplaintStatus.UNPROCESSED);
        }
        if (isComplaint && complaintCategoryIds != null && !complaintCategoryIds.isEmpty())
        {
            List<ComplaintCategory> categories = complaintCategoryRepository.findAllById(complaintCategoryIds);
            review.setComplaintCategories(categories);
        }

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


        java.time.LocalDate currentMonth = java.time.LocalDate.now();
        int updatedReviews =  productMonthlyStatRepository.incrementReviewsCount(product.getId(), currentMonth);
        if(updatedReviews == 0)
        {
            ProductMonthlyStat newStat  = new ProductMonthlyStat();
            newStat.setReviewsCount(1);
            newStat.setSalesCount(0);
            newStat.setProductId(product.getId());
            newStat.setStatDate(currentMonth); 

            productMonthlyStatRepository.save(newStat);
        }

        return savedReview;
    }
}
