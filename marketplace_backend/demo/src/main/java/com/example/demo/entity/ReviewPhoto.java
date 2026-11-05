package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "reviews_photo") 
public class ReviewPhoto
{
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn (name = "reviews_id", nullable = false)
    @JsonIgnore
    private Review reviewsId;
  
    @Column (name = "url")
    private String url;

    public ReviewPhoto(Long id, Review reviewsId, String url)
    {
        this.id = id;
        this.reviewsId = reviewsId;
        this.url = url;

    }
    public ReviewPhoto() {}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Review getReviewsId() {return reviewsId;}
    @JsonIgnore
    public void setReviewsId(Review reviewsId) {this.reviewsId = reviewsId;}
    
    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}
}
