package com.example.demo.dto;
import java.math.BigDecimal;
import java.util.Map;

import com.example.demo.entity.Product;
public class ProductCatalogDto
{
    private Long id;
    private String brand;
    private Long categoryId;
    private Double rating;
    private BigDecimal price;
    private String image;
    private String model;

    public ProductCatalogDto(Product product)
    {
        this.id  = product.getId();
        this.brand = product.getBrand();
        this.price = product.getPrice();
        this.rating = product.getRating();
        this.categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        this.model = product.getModel();

        if(product.getDetails() != null)
        {
            this.image = (String) product.getDetails().get("mainImage");
        }
    }
        public Long getId() {return id;}
        public void setId(Long id) {this.id = id;}

        public String getBrand() {return brand;}
        public void setBrand(String brand) {this.brand = brand;}

        public Long getCategoryId() {return categoryId;}
        public void setCategoryId(Long categoryId) {this.categoryId = categoryId;}

        public BigDecimal getPrice() {return price;}
        public void setPrice(BigDecimal price) {this.price = price;}

        public String getImage() {return image;}
        public void setImage(String image) {this.image = image;}

        public String getModel() {return model;}
        public void setModel(String model) {this.model = model;}

        public Double getRating() {return rating;}
        public void setRating(Double rating) {this.rating = rating;}
}
