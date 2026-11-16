package com.example.demo.dto;
import java.util.List;
public class VendorRatingDTO
{
    private double overallRating;
    private String statusLabel;
    private double productRating;
    private String onTimeDelivery;
    private String canceledPercent;
    private String loyaltyIndex;
    private int totalReviews;
    private List<Integer> starsDistribution;
    private List<Integer> radarMetrics;

    public double getOverallRating() { return overallRating; }
    public void setOverallRating(double overallRating) { this.overallRating = overallRating; }

    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }

    public double getProductRating() { return productRating; }
    public void setProductRating(double productRating) { this.productRating = productRating; }

    public String getOnTimeDelivery() { return onTimeDelivery; }
    public void setOnTimeDelivery(String onTimeDelivery) { this.onTimeDelivery = onTimeDelivery; }

    public String getCanceledPercent() { return canceledPercent; }
    public void setCanceledPercent(String canceledPercent) { this.canceledPercent = canceledPercent; }

    public String getLoyaltyIndex() { return loyaltyIndex; }
    public void setLoyaltyIndex(String loyaltyIndex) { this.loyaltyIndex = loyaltyIndex; }

    public int getTotalReviews() { return totalReviews; }
    public void setTotalReviews(int totalReviews) { this.totalReviews = totalReviews; }

    public List<Integer> getStarsDistribution() { return starsDistribution; }
    public void setStarsDistribution(List<Integer> starsDistribution) { this.starsDistribution = starsDistribution; }

     public List<Integer> getRadarMetrics() { return radarMetrics; }
    public void setRadarMetrics(List<Integer> radarMetrics) { this.radarMetrics = radarMetrics; }
}
