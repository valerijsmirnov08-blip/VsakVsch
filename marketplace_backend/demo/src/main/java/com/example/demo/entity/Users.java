package com.example.demo.entity;

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
private String userName;

@Column(nullable = false, unique = true)
private String email;

@Column(nullable = false)
private String password;


@Column (name = "phone", nullable = false)
private String phone;

@Column (name = "is_banned", nullable = false)
private Boolean isBanned = false;

@Enumerated (EnumType.STRING)
@Column (name = "role")
private Role role;

@Column (name ="hired")
private Long hired;



public Users() {}

public Long getId() {return id;}
public void setId(Long id) {this.id = id;}

public String getUserName() {return userName;}
public void setUserName(String userName) {this.userName = userName;}

public String getEmail() {return email;}
public void setEmail(String email) {this.email = email;}

public String getPhone() {return phone;}
public void setPhone(String phone) {this.phone = phone;}

public Boolean getIsBanned() {return isBanned;}
public void setIsBanned(Boolean isBanned) {this.isBanned = isBanned;}

public Role getRole() {return role;}
public void setRole(Role role) {this.role = role;}

public Long getHired() {return hired;}
public void setHired(Long hired) {this.hired = hired;}

public String getPassword() {return password;}
public void setPassword(String password) {this.password = password;}

// public Boolean getIsVendor() {return isVendor;}
// public void setIsVendor(Boolean isVendor) {this.isVendor = isVendor;}

// public String getShopName() {return ShopName;}
// public void setShopName(String ShopName) {this.ShopName = ShopName;}

// public String getShopDescription() {return ShopDescription;}
// public void setShopDescription(String ShopDescription) {this.ShopDescription = ShopDescription;}

// public String getBrandName() {return brandName;}
// public void setBrandName(String brandName) {this.brandName = brandName;}
}
