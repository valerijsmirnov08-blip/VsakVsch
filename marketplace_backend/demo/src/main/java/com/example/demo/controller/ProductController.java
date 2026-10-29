package com.example.demo.controller;
import com.example.demo.dto.ProductCatalogDto;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/products")

@CrossOrigin(origins = "http://localhost:5173")
public class ProductController
{
    @Autowired
    private ProductRepository productRepository;

    @Autowired 
    private ProductService productService;

    @GetMapping
    public Page<ProductCatalogDto> getProductsPage
    (   @RequestParam (defaultValue = "0") int page,
        @RequestParam (defaultValue = "20") int size
    )
        {
            return   productService.getProductsPage(page, size);
        }

    @GetMapping ("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id)
    {
        return productService.getProductById(id)
        .map(product -> ResponseEntity.ok().body(product))
        .orElse(ResponseEntity.notFound().build());
    }
    @CacheEvict (value = "productCache", allEntries = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id)
    {
        if(!productRepository.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);

        return ResponseEntity.ok("Товар успешно удален");
    } 
    @CacheEvict(value = "productCache", allEntries = true) 
    @PutMapping ("/{id}")
    public ResponseEntity<?> updateProductById(@PathVariable Long id, @RequestBody Product updatedData)
    {
        if(!productRepository.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }
        Optional<Product> productOpt = productRepository.findById(id);
        Product exisitingProduct = productOpt.get();
        exisitingProduct.setBrand(updatedData.getBrand());
        exisitingProduct.setModel(updatedData.getModel());
        exisitingProduct.setQuantity(updatedData.getQuantity());
        exisitingProduct.setDescription(updatedData.getDescription());
        exisitingProduct.setWarranty(updatedData.getWarranty());
        exisitingProduct.setEstimatedDelivery(updatedData.getEstimatedDelivery());
        exisitingProduct.setPrice(updatedData.getPrice()); 
        productRepository.save(exisitingProduct);
        return  ResponseEntity.ok("Товар успешно обнавлён");
    }
    @CacheEvict (value = "productCache", allEntries = true)
    @PostMapping ("/create")
    public ResponseEntity<?> createProduct(@RequestBody Product newProduct)
    {
        if(newProduct.getVendorId() == null || newProduct.getVendorSku() == null)
        {
            return ResponseEntity.badRequest().body("Ошибка: заполните все поля");
        }
        Product saveProduct = productRepository.save(newProduct);
        return ResponseEntity.ok(saveProduct);
    }
    @GetMapping ("/vendor/{vendorId}")
    public List<Product> findByVendorId(@PathVariable Long vendorId)
    {
        return productRepository.findByVendorId(vendorId);
    }
   @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts
    (
        @RequestParam String searchText,
        @RequestParam(required = false) Long categoryId)
    {
    
    List<Product> results = productService.search(searchText, categoryId);
    return ResponseEntity.ok(results);
    }
}





