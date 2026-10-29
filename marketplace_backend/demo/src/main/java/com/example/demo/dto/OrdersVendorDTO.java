package com.example.demo.dto;

import java.math.BigDecimal;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;


public class OrdersVendorDTO
{
    private Long id;
    private Product product;
    private Long vendorId;
    private BigDecimal price;
    private int quantity;
    private String status = "В процессе";
    private Boolean isReviewed = false;

    public OrdersVendorDTO(OrderItem order)
    {
        this.id = order.getId();
        this.product = order.getProduct();
        this.vendorId = order.getVendorId();
        this.price = order.getPrice();
        this.quantity = order.getQuantity();
        this.status = order.getStatus();
        this.isReviewed = order.getIsReviewed();
        
    }
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Product getProduct() {return product;}
    public void setProduct(Product product) {this.product = product;}
    
    public Long getVendorId() {return vendorId;}
    public void setVendorId(Long vendorId) {this.vendorId = vendorId;}

    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}

    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public Boolean getIsReviewed() {return isReviewed;}
    public void setIsReviewed(Boolean isReviewed) {this.isReviewed = isReviewed;}

}
