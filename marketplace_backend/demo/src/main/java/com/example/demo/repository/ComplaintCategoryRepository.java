package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.ComplaintCategory;

@Repository 
public interface ComplaintCategoryRepository extends  JpaRepository<ComplaintCategory, Long>
{
}
