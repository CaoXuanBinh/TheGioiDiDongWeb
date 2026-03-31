package com.hutech.CAOXUANBINH.controller;

import com.hutech.CAOXUANBINH.service.CategoryService;
import com.hutech.CAOXUANBINH.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Deprecated
@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/init")
    public ResponseEntity<Map<String, Object>> initData() {
        Map<String, Object> res = new HashMap<>();
        try {
            categoryService.ensureSampleCategories();
            productService.ensureSampleProducts();

            long catCount = categoryService.getAllCategories().size();
            long prodCount = productService.getAllProducts().size();

            res.put("categories", catCount);
            res.put("products", prodCount);
            res.put("status", "ok");
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            res.put("status", "error");
            res.put("message", ex.getMessage());
            return ResponseEntity.internalServerError().body(res);
        }
    }
}
