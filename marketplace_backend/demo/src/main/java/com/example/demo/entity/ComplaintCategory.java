package com.example.demo.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity 
@Table (name = "complaint_category")
public class ComplaintCategory
{
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore 
    private ComplaintCategory parent;

    public ComplaintCategory() {}
    public ComplaintCategory(Long id, String name, ComplaintCategory parent)
    {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }
    
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public ComplaintCategory getParent() {return parent;}
    public void setParent(ComplaintCategory parent) {this.parent = parent;}

    @JsonProperty("parentId")
    public Long getParentId() {
        return parent != null ? parent.getId() : null;
    }
}