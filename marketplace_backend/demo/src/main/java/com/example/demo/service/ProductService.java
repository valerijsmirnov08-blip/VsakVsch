package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.ProductRepository;
import com.example.demo.dto.ProductCatalogDto;
import com.example.demo.entity.Product;
import org.springframework.cache.annotation.Cacheable;
import java.util.List;
import java.util.Optional;

@Service 
public class ProductService
{
    @Autowired 
    private ProductRepository productRepository;

    public List<ProductCatalogDto> getAllProducts()
    {
        return productRepository.findAll().stream()
        .map(product -> new ProductCatalogDto(product))
        .toList();
    }
    @Cacheable (value = "productCache", key = "#id")
    public Optional<Product> getProductById(Long id)
    {
        return productRepository.findById(id);
    }
}
