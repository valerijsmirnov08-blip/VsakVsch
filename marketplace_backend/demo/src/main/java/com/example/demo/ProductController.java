package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")

@CrossOrigin(origins = "http://localhost:5173")
public class ProductController
{
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }
    @GetMapping ("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id)
    {
        return productRepository.findById(id)
        .map(product -> ResponseEntity.ok().body(product))
        .orElse(ResponseEntity.notFound().build());
    }
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
}





