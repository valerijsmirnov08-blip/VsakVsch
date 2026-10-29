package com.example.demo.dto;

import com.example.demo.entity.Favorite;
import com.example.demo.entity.Product;
import com.example.demo.entity.Users;


public class FavoriteDTO
{
    private Long id;
    private Product product;
    private Users user;
    public FavoriteDTO(Favorite favorite)
    {
        this.id = favorite.getId();
        this.product = favorite.getProduct();
        this.user = favorite.getUser();
    } 
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Users getUser() {return user;}
    public void setUser(Users user) {this.user = user;}

    public Product getProduct() {return product;}
    public void setProduct (Product product) {this.product = product;}
}
