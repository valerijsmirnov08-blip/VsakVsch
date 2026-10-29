package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;




@RestController 
@RequestMapping ("/api/category")
@CrossOrigin(origins = "http://localhost:5173")

public class CategoryController
{
    @Autowired
    public CategoryService categoryService;
    
    @GetMapping ("/roots")
    public  List<Category> getRootCategory()
    {
        return categoryService.getRootCategory();
    }

    @GetMapping ("/{id}/subcategories")
    public List<Category> getSubCategories(@PathVariable Long id)
    {
        return  categoryService.getSubCategories(id);
    }
}
