package com.hutech.CAOXUANBINH.config;

import com.hutech.CAOXUANBINH.service.CategoryService;
import com.hutech.CAOXUANBINH.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final ProductService productService;
    private final CategoryService categoryService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("========================================");
        System.out.println("Initializing sample data...");
        System.out.println("========================================");
        
        try {
            // Initialize categories first
            categoryService.ensureSampleCategories();
            System.out.println("✓ Categories initialized");
            
            // Then initialize products
            productService.ensureSampleProducts();
            System.out.println("✓ Products initialized");
            
            System.out.println("========================================");
            System.out.println("Sample data initialization completed!");
            System.out.println("========================================");
        } catch (Exception e) {
            System.out.println("❌ Error initializing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
