package com.hutech.CAOXUANBINH.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "categories")
@JsonIgnoreProperties({"products"})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Icon Bootstrap Icons, vi du: "bi-phone", "bi-laptop"
    private String icon;

    // Anh dai dien cua danh muc
    private String image;

    // Danh muc cha (null neu la cap 1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    // Danh muc con
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("name ASC")
    private List<Category> children = new ArrayList<>();

    // Danh sach san pham cua danh muc nay (dung cho menu preview)
    @OneToMany(mappedBy = "category")
    @OrderBy("id DESC")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

    // JPA/Hibernate can constructor rong de tao entity.
    public Category() {
    }

    // Constructor tien loi cho DataInitializer
    public Category(String name, String icon, Category parentCategory) {
        this.name = name;
        this.icon = icon;
        this.parentCategory = parentCategory;
    }
}
