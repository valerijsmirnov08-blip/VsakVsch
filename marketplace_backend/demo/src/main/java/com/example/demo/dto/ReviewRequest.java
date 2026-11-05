package com.example.demo.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReviewRequest
{
    private Long productId;
    private Long userId;
    private int rating;
    private String comment;
    private Long orderItemId;
    private List<String> photoUrl;
    @JsonProperty("isComplaint")
    private Boolean isComplaint;
    private List<Long> complaintCategoryIds;

    public ReviewRequest() {}

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Long getOrderItemId() {return orderItemId;}
    public void setOrderItemId(Long orderItemId) {this.orderItemId = orderItemId;}

    public List<String> getPhotoUrl() {return photoUrl;}
    public void setPhotoUrl(List<String> photoUrl) {this.photoUrl = photoUrl;}

    public boolean getIsComplaint() { return isComplaint; }
    public void setIsComplaint(boolean isComplaint) { this.isComplaint = isComplaint; }


    public List<Long> getComplaintCategoryIds() { return complaintCategoryIds; }
    public void setComplaintCategoryIds(List<Long> complaintCategoryIds) { this.complaintCategoryIds = complaintCategoryIds; }
}