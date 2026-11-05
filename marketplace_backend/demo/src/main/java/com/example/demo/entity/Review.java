package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.ArrayList;

@Entity 
@Table (name = "reviews")

public class Review
{
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "product_id", nullable = false)
    private Long productId;

    @Column (name = "user_id", nullable = false)
    private Long userId;

    @Column (name = "rating", nullable = false)
    private int rating;

    @Column (name = "comment", nullable = true)
    private String comment;

    @Column (name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column (name = "is_read", nullable = true)
    private Boolean isRead = false;

    @ManyToOne
    @JoinColumn (name = "order_item_id", nullable = false)
    private OrderItem orderItemId;

    @OneToMany (mappedBy = "reviewsId", cascade = CascadeType.ALL)
    private List<ReviewPhoto> photos = new ArrayList<>(); 

    @Column (name = "is_complaint", nullable = false)
    private Boolean isComplaint = false;
    
    @ManyToMany
    @JoinTable(
        name = "review_complaint_categories",
        joinColumns = @JoinColumn(name = "review_id"),
        inverseJoinColumns = @JoinColumn(name = "complaint_category_id")
    )
    private List<ComplaintCategory> complaintCategories = new ArrayList<>();


    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getProductId() {return productId;}
    public void setProductId(Long productId) {this.productId = productId;}

    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}

    public int getRating() {return rating;}
    public void setRating(int rating) {this.rating = rating;}

    public String getComment() {return comment;}
    public void setComment(String comment) {this.comment = comment;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public OrderItem getOrderItemId() {return orderItemId;}
    public void setOrderItemId(OrderItem orderItemId) {this.orderItemId = orderItemId;}

    public List<ReviewPhoto> getPhotos() {return photos;}
    public void setPhotos(List<ReviewPhoto> photos) {this.photos = photos;}

    public Boolean getIsComplaint() { return isComplaint; }
    public void setIsComplaint(Boolean isComplaint) { this.isComplaint = isComplaint; }

    public List<ComplaintCategory> getComplaintCategories() { return complaintCategories; }
    public void setComplaintCategories(List<ComplaintCategory> complaintCategories) { this.complaintCategories = complaintCategories; }
}
