package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.VendorRatingDTO;
import com.example.demo.entity.Review;
import com.example.demo.entity.OrderItem;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;
@Service 
public class VendorRatingService
{
    @Autowired 
    private ReviewRepository reviewRepository;
    @Autowired 
    private OrderItemRepository orderItemRepository;

    public VendorRatingDTO getVendorRatingDTO(Long vendorId, String period)
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = "Месяц".equals(period) ? now.minusMonths(1)
        : ("Полгода".equals(period) ? now.minusMonths(6) : LocalDateTime.of(2000, 1, 1, 0, 0));

        List<Review> reviews = reviewRepository.findByVendorId(vendorId).stream()
        .filter(r -> r.getCreatedAt() != null && r.getCreatedAt().isAfter(startDate)).toList();
    
        List<OrderItem> items = orderItemRepository.findByVendorId(vendorId).stream()
        .filter(i -> i.getOrder() != null && i.getOrder().getCreatedAt() != null && i.getOrder().getCreatedAt().isAfter(startDate)).toList();

        int totalReviews = reviews.size();
        double rawProductRating = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        double productRating = Math.round(rawProductRating * 100.0) / 100.0;

        List<Integer> starsDistribution = new ArrayList<>();
        for(int s = 5; s >= 1; s--)
        {
            final int star = s;
            starsDistribution.add((int) reviews.stream().filter(r -> r.getRating() == star).count());
        }
        int loyaltyPct = totalReviews == 0 ? 100 : (int) Math.round((starsDistribution.get(0) + starsDistribution.get(1)) * 100.0 / totalReviews);
        int totalItems = items.size();
        long canceledCount = items.stream().filter(i -> "Отменен".equalsIgnoreCase(i.getStatus())).count();
        double canceledPct = totalItems == 0 ? 0.0 : Math.round((canceledCount * 100.0 / totalItems) * 10.0) / 10.0;
        int onTimePct = totalItems == 0 ? 100 : (int) Math.round((totalItems - canceledCount) * 100.0 / totalItems);
        
        int productScore = totalReviews == 0 ? 100 : (int) Math.round(rawProductRating * 20.0);
        long complaints = reviews.stream().filter(r -> Boolean.TRUE.equals(r.getIsComplaint())).count();
        int noComplaintsScore = totalReviews == 0 ? 100 : (int) Math.round((totalReviews - complaints) * 100.0 / totalReviews);
        long readReviews = reviews.stream().filter(r -> Boolean.TRUE.equals(r.getIsRead())).count();
        int responseScore = totalReviews == 0 ? 100 : (int) Math.round(readReviews * 100.0 / totalReviews);
        List<Integer> radarMetrics = List.of(productScore, onTimePct, (int) Math.round(100.0 - canceledPct), noComplaintsScore, responseScore);
        double avgRadar = radarMetrics.stream().mapToInt(Integer::intValue).average().orElse(100.0);
        double overallRating = Math.round(( avgRadar / 20.0) * 10.0 ) / 10.0;

        String statusLabel = overallRating >= 4.7 ? "Надёжный продавец" : (overallRating >= 4.0 ? "Хороший рейтинг" : "Требует внимания");
    
    
        VendorRatingDTO dto = new VendorRatingDTO();
        dto.setOverallRating(overallRating);
        dto.setStatusLabel(statusLabel);
        dto.setProductRating(productRating);
        dto.setOnTimeDelivery(onTimePct + "%");
        dto.setCanceledPercent(canceledPct + "%");
        dto.setLoyaltyIndex(loyaltyPct + "%");
        dto.setTotalReviews(totalReviews);
        dto.setStarsDistribution(starsDistribution);
        dto.setRadarMetrics(radarMetrics);
        return dto;
    }
}
