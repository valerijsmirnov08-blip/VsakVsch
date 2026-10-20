package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "month_stat")

public class ProductMonthlyStat
{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column (name = "product_id", nullable = false)
    private Long productId;

    @Column (name = "month_label", nullable = false)
    private String monthLabel;

    @Column (name = "sales_сount", nullable = false)
    private Integer salesCount = 0;

    @Column (name = "reviews_сount", nullable = false)
    private Integer reviewsCount = 0;

    public ProductMonthlyStat(){}
    public ProductMonthlyStat(Long id, Long productId, String monthLabel, Integer salesCount, Integer reviewsCount)
    {
        this.id = id;
        this.productId = productId;
        this.monthLabel = monthLabel;
        this.salesCount = salesCount;
        this.reviewsCount = reviewsCount;
    }

    public Long getId() {return id; }
    public void setId(Long id) {this.id = id;}

    public Long getProductId() {return productId; }
    public void setProductId(Long productId) {this.productId = productId;}

    public String getMonthLabel() {return monthLabel; }
    public void setMonthLabel(String monthLabel) {this.monthLabel = monthLabel;}

    public Integer getSalesCount() {return salesCount; }
    public void setSalesCount(Integer salesCount) {this.salesCount = salesCount;}

    public Integer getReviewsCount() {return reviewsCount; }
    public void setReviewsCount(Integer reviewsCount) {this.reviewsCount = reviewsCount;}
}
