package com.example.demo.dto;
import java.util.List;

public class ProductStatisticDTO
{
    private List<String> labels;
    private List<Integer> sales;
    private List<Integer> reviews;

    public List<String> getLabels() {return labels;}
    public void setLabels(List<String> labels) {this.labels = labels;}
    
    public List<Integer> getSales() {return sales;}
    public void setSales(List<Integer> sales) {this.sales = sales;}

    public List<Integer> getReviews() {return reviews;}
    public void setReviews(List<Integer> reviews) {this.reviews = reviews;}
}
