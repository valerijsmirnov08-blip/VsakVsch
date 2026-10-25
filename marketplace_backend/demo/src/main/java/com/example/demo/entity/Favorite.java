package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "favorite")
public class Favorite
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    public Favorite() {}
    public Favorite(Users user, Product product)
    {
        this.user = user;
        this.product = product;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Users getUser() {return user;}
    public void setUser(Users user) {this.user = user;}

    public Product getProduct() {return product;}
    public void setProduct (Product product) {this.product = product;}
}
