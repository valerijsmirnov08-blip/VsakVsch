package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "vendor_profile")
public class VendorProfile
{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne 
    @JoinColumn (name = "vendor_id", nullable = false)
    private Users vendor;

    @Column (name = "shop_name", nullable = false)
    private String shopName;

    @Column (name = "brand_name")
    private String brandName;

    @Column (name = "shop_description")
    private String shopDesc;

    @Column (name = "inn", nullable = false)
    private String inn;

    @Column (name = "rating_vendor", nullable = false)
    private Double ratingVendor = 0.0;

    @Column (name = "legal_address", nullable = false)
    private String legalAddress;

    @Column (name = "logo_url")
    private String logoUrl;

    public VendorProfile () {}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Users getVendor() {return vendor;}
    public void setVendor(Users vendor) {this.vendor = vendor;}

    public String getShopName() {return shopName;}
    public void setShopName(String shopName) {this.shopName = shopName;}

    public String getBrandName() {return brandName;}
    public void setBrandName(String brandName) {this.brandName = brandName;}

    public String getShopDesc() {return shopDesc;}
    public void setShopDesc(String shopDesc) {this.shopDesc = shopDesc;}

    public String getInn() {return inn;}
    public void setInn(String inn) {this.inn = inn;}

    public Double getRatingVendor() {return ratingVendor;}
    public void setRatingVendor(Double ratingVendor) {this.ratingVendor = ratingVendor;}

    public String getLegalAddress() {return legalAddress;}
    public void setLegalAddress(String legalAddress) {this.legalAddress = legalAddress;}

    public String getLogoUrl() {return logoUrl;}
    public void setLogoUrl(String logoUrl) {this.logoUrl = logoUrl;}
}
