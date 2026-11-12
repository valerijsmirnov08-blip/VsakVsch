package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.ProductRepository;
import com.example.demo.dto.ProductCatalogDto;
import com.example.demo.entity.Product;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<ProductCatalogDto> getProductsPage(int page, int size)
    {
        return productRepository.findAll(PageRequest.of(page, size))
        .map(product -> new ProductCatalogDto(product));
    }



    public List<Product> search(String searchText, Long categoryId)
    {
        if(searchText == null || searchText.trim().length() < 2)
        {
            return List.of();
        }
        try
        {
            Long productId = Long.parseLong(searchText.trim());
            Optional<Product> exactMath = productRepository.findById(productId);
            if(exactMath.isPresent())
            {
                return List.of(exactMath.get());
            }
        }
        catch(NumberFormatException a)
        {

        }
        String query = searchText.toLowerCase().trim();
        return productRepository.searchProducts(query, categoryId);
    }
}
