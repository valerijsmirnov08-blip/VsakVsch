package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Category;

import java.util.List;

@Repository 
public interface  CategoryRepository extends  JpaRepository <Category, Long>
{
    List<Category> findByParentIsNull();
    List<Category> findByParentId(Long parentId);
}
