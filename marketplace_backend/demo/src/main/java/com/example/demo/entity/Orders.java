package com.example.demo.entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity 
@Table (name = "orders", indexes = {@Index (name = "idx_orders_user_id", columnList = "user_id")})
public class Orders
{
@Id 
@GeneratedValue (strategy = GenerationType.IDENTITY)
private Long id;

@Column(name = "user_id", nullable = false)
private Long userId;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", insertable = false, updatable = false)
private Users user;

@Column(name = "total_price", nullable = false)
private BigDecimal totalPrice;

@Column (nullable = false)
private String status = "В процессе";

@Column ( name = "created_at" ,nullable = false)
private LocalDateTime createdAt ;



public Orders() {}
public Orders(Long userId, BigDecimal total_price, String status, LocalDateTime created_at)
{
    this.userId = userId;
    this.totalPrice = total_price;
    this.status = status;
    this.createdAt = created_at;
}

public Long getId() {return id;}
public void setId(Long id) {this.id = id;}

public Long getUserId() { return userId; }
public void setUserId(Long userId) { this.userId = userId; }

public BigDecimal getTotalPrice() {return totalPrice;}
public void setTotalPrice (BigDecimal total_price) {this.totalPrice = total_price;}

public String getStatus() {return status;}
public void setStatus(String status) {this.status = status;}

public LocalDateTime getCreatedAt() {return createdAt;}
public void setCreatedAt(LocalDateTime created_at) {this.createdAt = created_at;}

public Users getUser() { return user; }
public void setUser(Users user) { this.user = user; }
}