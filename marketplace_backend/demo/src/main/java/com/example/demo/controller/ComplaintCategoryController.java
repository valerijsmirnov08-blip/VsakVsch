package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entity.ComplaintCategory;
import com.example.demo.repository.ComplaintCategoryRepository;
import java.util.List;



@RestController 
@RequestMapping ("/api/categories")
@CrossOrigin(origins = "http://localhost:5173")
public class ComplaintCategoryController
{
    @Autowired 
    public ComplaintCategoryRepository categoryRepository;

    @GetMapping ("/complaints")
    public  List<ComplaintCategory> getRootCategory()
    {
        return categoryRepository.findAll();
    }
}
