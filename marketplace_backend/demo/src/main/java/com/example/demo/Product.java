package com.example.demo;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Column(name = "vendor_sku", nullable = false)
    private String vendorSku;

    private String brand;
    private String model;
    private BigDecimal price;
    private Double rating;

    @Column(name = "estimated_delivery")
    private String estimatedDelivery;

    private String warranty;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "main_image")
    private String mainImage;

    @Column (columnDefinition = "TEXT")
    private String gallery;

    @Column (nullable = false)
    private int quantity;

    private String status;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    
    public Long getVendorId() {return vendorId;}
    public void setVendorId(Long vendorId) {this.vendorId = vendorId;}
    
    public String getVendorSku() {return vendorSku;}
    public void setVendorSku(String vendorSku) {this.vendorSku = vendorSku;}
    
    public String getBrand() {return brand;}
    public void setBrand(String brand) {this.brand = brand;}

    public String getModel() {return model;}
    public void setModel(String model) {this.model = model;}

    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}

    public Double getRating() {return rating;}
    public void setRating(Double rating) {this.rating = rating;}

    public String getEstimatedDelivery() {return estimatedDelivery;}
    public void setEstimatedDelivery(String estimatedDelivery) {this.estimatedDelivery = estimatedDelivery;}

    public String getWarranty() {return warranty;}
    public void setWarranty(String warranty) {this.warranty = warranty;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getMainImage() {return mainImage;}
    public void setMainImage(String mainImage) {this.mainImage = mainImage;}

    public String getGallery() {return gallery;}
    public void setGallery(String gallery) {this.gallery = gallery;}

    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
}