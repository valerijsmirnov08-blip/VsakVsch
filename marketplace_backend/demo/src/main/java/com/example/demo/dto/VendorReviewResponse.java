package com.example.demo.dto;

import java.time.LocalDateTime;

public class VendorReviewResponse {
    private Long id;
    private Long productId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private boolean isRead;
    
    private java.util.List<String> photoUrls = new java.util.ArrayList<>();

    public VendorReviewResponse(Long id, Long productId, int rating, String comment, LocalDateTime createdAt, boolean isRead) {
        this.id = id;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public VendorReviewResponse(Long id, Long productId, int rating, String comment, LocalDateTime createdAt, boolean isRead, java.util.List<String> photoUrls) {
        this.id = id;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.photoUrls = photoUrls;
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean getIsRead() { return isRead; }
    public java.util.List<String> getPhotoUrls() { return photoUrls; }
    public void setPhotoUrls(java.util.List<String> photoUrls) { this.photoUrls = photoUrls; }
}