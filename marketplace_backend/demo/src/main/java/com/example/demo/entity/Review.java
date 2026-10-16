package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
}
