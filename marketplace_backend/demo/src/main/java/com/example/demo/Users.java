package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;


@Entity 
@Table (name = "users")
public class Users 
{
@Id 
@GeneratedValue (strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false, unique = true)
@JsonProperty ("userName")
private String user_name;

@Column(nullable = false, unique = true)
private String email;

@Column(nullable = false)
private String password;

@Column(name = "is_vendor")
private Boolean isVendor = false;

@Column(name = "shop_name")
private String ShopName;

@Column(name = "shop_description")
private String ShopDescription;

public Users() {}

public Long getId() {return id;}
public void setId(Long id) {this.id = id;}

public String getUserName() {return user_name;}
public void setUserName(String user_name) {this.user_name = user_name;}

public String getEmail() {return email;}
public void setEmail(String email) {this.email = email;}

public String getPassword() {return password;}
public void setPassword(String password) {this.password = password;}

public Boolean getIsVendor() {return isVendor;}
public void setIsVendor(Boolean isVendor) {this.isVendor = isVendor;}

public String getShopName() {return ShopName;}
public void setShopName(String ShopName) {this.ShopName = ShopName;}

public String getShopDescription() {return ShopDescription;}
public void setShopDescription(String ShopDescription) {this.ShopDescription = ShopDescription;}
}
