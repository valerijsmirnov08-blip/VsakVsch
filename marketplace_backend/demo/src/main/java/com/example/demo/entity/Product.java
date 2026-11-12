package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.Map;

@Entity
@Table(name = "products")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column (name = "brand", nullable = false)
    private String brand;

    @Column (name = "model", nullable = false)
    private String model;

    @Column (name = "warranty", nullable = false)
    private String warranty;

    @Column (name ="description")
    private String description;

    @Column (name = "price", nullable = false)
    private BigDecimal price;

    @Column (name = "quantity", nullable = false)
    private Integer quantity;

    @Column (name = "rating", nullable = false)
    private Double rating = 0.0;

    @Column(name = "vendor_sku")
    private String vendorSku;

    @ManyToOne 
    @JoinColumn (name = "category_id", nullable = false)
    private Category category;

    @ManyToOne 
    @JoinColumn (name = "vendor_id", nullable = false)
    private Users vendor;

    @JdbcTypeCode (SqlTypes.JSON)
    @Column (name = "details", columnDefinition = "jsonb")
    private Map<String, Object> details;
    
    public Product (){}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getBrand() {return brand;}
    public void setBrand(String brand) {this.brand = brand;}

    public String getModel() {return model;}
    public void setModel(String model) {this.model = model;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getWarranty() {return warranty;}
    public void setWarranty(String warranty) {this.warranty = warranty;}

    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}

    public Integer getQuantity() {return quantity;}
    public void setQuantity(Integer quantity) {this.quantity = quantity;}

    public Double getRating() {return rating;}
    public void setRating(Double rating) {this.rating = rating;}

    public String getVendorSku() {return vendorSku;}
    public void setVendorSku(String vendorSku) {this.vendorSku = vendorSku;}

    public Category getCategory() {return category;}
    public void setCategory(Category category) {this.category = category;}

    public Users getVendor() {return vendor;}
    public void setVendor(Users vendor) {this.vendor = vendor;}

    public Map<String, Object> getDetails() {return details;}
    public void setDetails(Map<String, Object> details) {this.details = details;}

}