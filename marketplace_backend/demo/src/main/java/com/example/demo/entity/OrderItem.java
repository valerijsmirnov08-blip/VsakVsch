package com.example.demo.entity;
import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity 
@Table (name = "order_items")

public class OrderItem
{
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    
    @ManyToOne 
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column (name = "vendor_id", nullable = false)
    private Long vendorId;

    @Column (name = "price", nullable = false)
    private BigDecimal price;

    @Column (name = "quantity", nullable = false)
    private int quantity;

    @Column  (name = "status", nullable = false)
    private String status = "В процессе";

    @Column(name = "is_reviewed")
    private Boolean isReviewed = false;



    public OrderItem(){}
    public OrderItem(Long id, Orders order, Product product, Long vendorId, BigDecimal price, int quantity, String status)
    {
        this.id = id;
        this.order = order;
        this.product = product;
        this.vendorId = vendorId;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

    public Long getId() {return  id;}
    public void setId(Long id) {this.id = id;}

    public Orders getOrder() {return order;}
    public void setOrder(Orders order) {this.order = order;}
    
    public Product getProduct() {return product;}
    public void setProduct(Product product) {this.product = product;}
    
    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}

    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public Long getVendorId() {return vendorId;}
    public void setVendorId(Long vendorId) {this.vendorId = vendorId;}

    public Boolean getIsReviewed() {return isReviewed;}
    public void setIsReviewed(Boolean isReviewed) {this.isReviewed = isReviewed;}
}
