package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.CategoryRepository;
@Service 
public class CategoryService
{
    @Autowired
    public CategoryRepository categoryRepository;

    public List<Category> getRootCategory()
    {
        return categoryRepository.findByParentIsNull();

    }
    public List<Category> getSubCategories(Long parentId)
    {
        return  categoryRepository.findByParentId(parentId);
    }
}
