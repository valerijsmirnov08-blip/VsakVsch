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
        if (searchText == null || searchText.trim().length() < 2) {
            return List.of();
        }
        
        String query = searchText.toLowerCase().trim();
        List<Product> productsToSearch = (categoryId != null) 
            ? productRepository.findByCategory_Id(categoryId) 
            : productRepository.findAll();

        return productsToSearch.stream()
            .filter(product -> isSmartMatch(product.getModel(), query) || 
                               isSmartMatch(product.getVendorSku(), query))
            .limit(10)
            .toList();
    }
    private boolean isSmartMatch(String target, String query) {
        if (target == null) return false;
        String targetLower = target.toLowerCase();
        if (targetLower.contains(query)) return true;
        String[] targetWords = targetLower.split("\\s+");
        String[] queryWords = query.split("\\s+");

        for (String qWord : queryWords) {
            for (String tWord : targetWords) {
                if (calculateLevenshtein(qWord, tWord) <= 2) {
                    return true;
                }
            }
        }
        return false;
    }

    private int calculateLevenshtein(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else {
                    int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }
    // {
    //     if (searchText == null || searchText.trim().isEmpty()) {
    //     return List.of();
    // }
    
    // String[] words = searchText.trim().split("\\s+");
    // StringBuilder booleanQuery = new StringBuilder();
    
    // for (String word : words) {
    //     if (word.length() >= 3) { 
    //         booleanQuery.append(word).append("* ");
    //     }
    // }
    
    // if (booleanQuery.length() == 0) {
    //     return List.of();
    // }
    
    // return productRepository.searchProducts(booleanQuery.toString().trim(), categoryId);
    // }
}
