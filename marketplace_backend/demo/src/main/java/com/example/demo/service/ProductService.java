package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.repository.CartRepository;
import com.example.demo.repository.FavoriteRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.dto.ProductCatalogDto;
import com.example.demo.entity.Product;

import org.springframework.cache.annotation.CacheEvict;
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
    @Autowired 
    private FavoriteRepository favoriteRepository;
    @Autowired 
    private CartRepository cartRepository;

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
        String query = searchText == null ? "" : searchText.toLowerCase().trim();
        if (categoryId == null && query.length() < 2)
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
        return productRepository.searchProducts(query, categoryId);
    }
    @Transactional 
    @CacheEvict(value = "productCache", key = "#id")
    public void deleteProduct(Long id)
    {
        favoriteRepository.deleteByProductId(id);
        cartRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }
}
