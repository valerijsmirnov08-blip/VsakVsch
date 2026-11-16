package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "conversations")
public class Conversation
{
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn (name = "buyer_id")
    private Users buyer;

    @ManyToOne 
    @JoinColumn (name = "vendor_id", nullable = false)
    private Users vendor;

    @ManyToOne 
    @JoinColumn (name = "product_id")
    private Product product;

    @Column (name = "type", nullable = false)
    private String type = "PRODUCT";

    @Column(name = "guest_email")
    private String guestEmail;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Conversation() {}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Users getBuyer() {return buyer;}
    public void setBuyer(Users buyer) {this.buyer = buyer;}

    public Users getVendor() {return vendor;}
    public void setVendor(Users vendor) {this.vendor = vendor;}

    public Product getProduct() {return product;}
    public void setProduct(Product product) {this.product = product;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getGuestEmail() {return guestEmail;}
    public void setGuestEmail(String guestEmail) {this.guestEmail = guestEmail;}

    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}
}
