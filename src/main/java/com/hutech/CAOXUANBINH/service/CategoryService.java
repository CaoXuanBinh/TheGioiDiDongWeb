package com.hutech.CAOXUANBINH.service;

import com.hutech.CAOXUANBINH.model.Category;
import com.hutech.CAOXUANBINH.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@SuppressWarnings("null")
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // Lấy tất cả danh mục
    public List<Category> getAllCategories() {
        ensureSampleCategories();
        return categoryRepository.findAll();
    }

    // Đảm bảo có dữ liệu mẫu
    public void ensureSampleCategories() {
        if (categoryRepository.count() > 0) {
            return;
        }
        seedDefaultCategories();
    }

    // Lấy danh mục cấp 1 (kèm danh mục con do EAGER loading)
    public List<Category> getRootCategories() {
        ensureSampleCategories();
        return categoryRepository.findByParentCategoryIsNull();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category saveCategory(Category category, org.springframework.web.multipart.MultipartFile imageFile)
            throws java.io.IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageFileName = saveImage(imageFile);
            category.setImage(imageFileName);
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category, org.springframework.web.multipart.MultipartFile imageFile)
            throws java.io.IOException {
        Category existingCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new IllegalStateException("Category not found"));

        existingCategory.setName(category.getName());
        existingCategory.setIcon(category.getIcon());
        existingCategory.setParentCategory(category.getParentCategory());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageFileName = saveImage(imageFile);
            existingCategory.setImage(imageFileName);
        }

        return categoryRepository.save(existingCategory);
    }

    public void deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalStateException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    private String saveImage(org.springframework.web.multipart.MultipartFile imageFile) throws java.io.IOException {
        String uploadDir = "src/main/resources/static/images/";
        java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);

        if (!java.nio.file.Files.exists(uploadPath)) {
            java.nio.file.Files.createDirectories(uploadPath);
        }

        String fileName = java.util.UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        java.nio.file.Path filePath = uploadPath.resolve(fileName);

        java.nio.file.Files.copy(imageFile.getInputStream(), filePath,
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    private List<Category> seedDefaultCategories() {
        Category phone = categoryRepository.save(new Category("Dien thoai", "bi-phone", null));
        Category laptop = categoryRepository.save(new Category("Laptop", "bi-laptop", null));
        Category tablet = categoryRepository.save(new Category("May tinh bang", "bi-tablet", null));
        Category watch = categoryRepository.save(new Category("Dong ho", "bi-watch", null));
        Category accessory = categoryRepository.save(new Category("Phu kien", "bi-headphones", null));

        categoryRepository.saveAll(List.of(
                new Category("iPhone", "bi-apple", phone),
                new Category("Samsung", "bi-phone-flip", phone),
                new Category("Xiaomi", "bi-phone-vibrate", phone),
                new Category("OPPO", "bi-phone-landscape", phone),
                new Category("Realme", "bi-phone", phone),
                new Category("MacBook", "bi-apple", laptop),
                new Category("Laptop Dell", "bi-pc-display", laptop),
                new Category("Laptop ASUS", "bi-laptop", laptop),
                new Category("Laptop Lenovo", "bi-pc", laptop),
                new Category("Laptop HP", "bi-display", laptop),
                new Category("iPad", "bi-apple", tablet),
                new Category("Samsung Tab", "bi-tablet-landscape", tablet),
                new Category("Xiaomi Pad", "bi-tablet", tablet),
                new Category("Apple Watch", "bi-apple", watch),
                new Category("Samsung Watch", "bi-watch", watch),
                new Category("Garmin", "bi-smartwatch", watch),
                new Category("Tai nghe Bluetooth", "bi-earbuds", accessory),
                new Category("Tai nghe chup tai", "bi-headphones", accessory),
                new Category("Loa", "bi-speaker", accessory),
                new Category("Camera Giam Sat", "bi-camera-video", accessory),
                new Category("Camera ngoai troi", "bi-webcam", accessory),
                new Category("Chuot gaming", "bi-mouse", accessory),
                new Category("Ban phim gaming", "bi-keyboard", accessory),
                new Category("Tai nghe gaming", "bi-headset", accessory),
                new Category("O cung di dong", "bi-hdd", accessory),
                new Category("The nho", "bi-sd-card", accessory),
                new Category("USB", "bi-usb-drive", accessory),
                new Category("Sac & Cap", "bi-plug", accessory),
                new Category("Op lung", "bi-phone", accessory),
                new Category("Pin du phong", "bi-battery-charging", accessory)
        ));

        return categoryRepository.findAll();
    }
}
