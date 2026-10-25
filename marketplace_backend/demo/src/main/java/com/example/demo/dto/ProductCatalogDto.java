package com.example.demo.dto;
import java.math.BigDecimal;
import com.example.demo.entity.Product;
public class ProductCatalogDto
{
    private Long id;
    private String brand;
    private String model;
    private BigDecimal price;
    
    public ProductCatalogDto(Product product)
    {
        this.id  = product.getId();
        this.brand = product.getBrand();
        this.model = product.getModel();
        this.price = product.getPrice();

       
    }
        public Long getId() {return id;}
        public void setId(Long id) {this.id = id;}

        public String getBrand() {return brand;}
        public void setBrand(String brand) {this.brand = brand;}

        public String getModel() {return model;}
        public void setModel(String model) {this.model = model;}

        public BigDecimal getPrice() {return price;}
        public void setPrice(BigDecimal price) {this.price = price;}
}
