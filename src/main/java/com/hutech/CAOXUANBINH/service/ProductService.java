package com.hutech.CAOXUANBINH.service;

import com.hutech.CAOXUANBINH.model.Category;
import com.hutech.CAOXUANBINH.model.Product;
import com.hutech.CAOXUANBINH.repository.CategoryRepository;
import com.hutech.CAOXUANBINH.repository.OrderDetailRepository;
import com.hutech.CAOXUANBINH.repository.ProductRepository;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderDetailRepository orderDetailRepository;

    public void ensureSampleProducts() {
        try {
            long current = productRepository.count();
            System.out.println("[ProductService] current product count = " + current);
            if (current > 0) {
                return;
            }

            List<Category> categories = categoryRepository.findAll();
            System.out.println("[ProductService] current category count = " + categories.size());
            if (categories.isEmpty()) {
                categories = seedDefaultCategories();
                System.out.println("[ProductService] seeded categories = " + categories.size());
            }

            Category catIphone = findCategory(categories, "iPhone");
            Category catSamsung = findCategory(categories, "Samsung");
            Category catXiaomi = findCategory(categories, "Xiaomi");
            Category catOppo = findCategory(categories, "OPPO");
            Category catRealme = findCategory(categories, "Realme");
            Category catMac = findCategory(categories, "MacBook");
            Category catDell = findCategory(categories, "Laptop Dell");
            Category catAsus = findCategory(categories, "Laptop ASUS");
            Category catLenovo = findCategory(categories, "Laptop Lenovo");

            productRepository.saveAll(List.of(
                    createSampleProduct("iPhone 16 Pro Max", 33990000, "Flagship moi nhat tu Apple", "iphone16promax.png", catIphone, true, 10),
                    createSampleProduct("iPhone 15 Pro", 27990000, "Apple iPhone 15 Pro 256GB", "iphone15pro.jpg", catIphone, false, 0),
                    createSampleProduct("Samsung Galaxy S25 Ultra", 31990000, "Samsung S25 Ultra 512GB", "dfda4936-393b-49df-b252-c89bc89cc4bc_samsunggalaxys25utra.jpg", catSamsung, true, 15),
                    createSampleProduct("Samsung Galaxy A55", 9490000, "Samsung A55 5G 128GB", "samsunggalaxyA55.jpg", catSamsung, false, 0),
                    createSampleProduct("Xiaomi 14T Pro", 18990000, "Xiaomi 14T Pro 12GB/512GB", "5de116ef-760d-4213-8885-b2c76518c9e9_Xiaomi 14T Pro.jpg", catXiaomi, true, 20),
                    createSampleProduct("OPPO Find X8", 21990000, "OPPO Find X8 5G 256GB", "iphone16promax.png", catOppo, false, 0),
                    createSampleProduct("Realme GT 7 Pro", 14990000, "Realme GT 7 Pro 256GB", "faf0bde3-fa8f-4eb8-9aaa-34b88f59051a_Realme GT 7 Pro.jpg", catRealme, true, 5),
                    createSampleProduct("MacBook Pro M4", 54990000, "MacBook Pro 14 inch M4 2024", "93c01d36-be4b-4ce8-9996-14d15e01a3e2_MacBook Pro M4.jpg", catMac, true, 8),
                    createSampleProduct("MacBook Air M3", 32990000, "MacBook Air 13 inch M3", "98294152-2f10-468b-9a1b-cf0297b0ee2a_MacBook Air M3.jpg", catMac, false, 0),
                    createSampleProduct("Dell XPS 15", 45990000, "Dell XPS 15 OLED i9 RTX 4070", "355f9dc9-fcd2-4079-ac97-33a360f19031_Dell XPS 15.jpg", catDell, true, 12),
                    createSampleProduct("ASUS ROG Zephyrus G14", 42990000, "ASUS ROG Zephyrus G14 RTX 4070", "7f6c0479-b9ee-4d17-9649-dd35a65a2b62_ASUS ROG Zephyrus G14.jpg", catAsus, false, 0),
                    createSampleProduct("Lenovo ThinkPad X1 Carbon", 38990000, "Lenovo ThinkPad X1 Carbon 2024", "21ee60d7-a357-417c-a7d6-0adf6878574b_Lenovo ThinkPad X1 Carbon.jpg", catLenovo, true, 7)
            ));

            System.out.println("[ProductService] seeded products. new count = " + productRepository.count());
        } catch (Exception ex) {
            System.out.println("[ProductService] Error during seeding: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public List<Product> getAllProducts() {
        ensureSampleProducts();
        return productRepository.findAll();
    }

    public List<Product> getPromotionProducts() {
        ensureSampleProducts();
        return productRepository.findByPromotionTypeIn(java.util.Arrays.asList("DISCOUNT", "GIFT"));
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        ensureSampleProducts();
        if (categoryId == null) {
            return productRepository.findAll();
        }
        return productRepository.findByCategoryIdOrParentId(categoryId);
    }

    public Optional<Product> getProductById(@NonNull Long id) {
        ensureSampleProducts();
        return productRepository.findById(id);
    }

    public Product addProduct(@NonNull Product product) {
        try {
            return addProduct(product, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Product addProduct(@NonNull Product product, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageFileName = saveImage(imageFile);
            product.setImage(imageFileName);
        }
        return productRepository.save(product);
    }

    public Product updateProduct(@NotNull Product product) {
        try {
            return updateProduct(product, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Product updateProduct(@NotNull Product product, MultipartFile imageFile) throws IOException {
        Long id = product.getId();
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null for update.");
        }

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product with ID " + id + " does not exist."));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setPromotionType(product.getPromotionType());
        existingProduct.setDiscountPercent(product.getDiscountPercent());
        existingProduct.setGiftDescription(product.getGiftDescription());
        existingProduct.setStockQuantity(product.getStockQuantity());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageFileName = saveImage(imageFile);
            existingProduct.setImage(imageFileName);
        }

        return productRepository.save(existingProduct);
    }

    public void decrementStock(Long productId, int qty) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Product not found: " + productId));
        int newQty = Math.max(0, product.getStockQuantity() - qty);
        product.setStockQuantity(newQty);
        productRepository.save(product);
    }

    public void deleteProductById(@NonNull Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        orderDetailRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        String uploadDir = "src/main/resources/static/images/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    private Category findCategory(List<Category> categories, String name) {
        return categories.stream()
                .filter(category -> name.equals(category.getName()))
                .findFirst()
                .orElse(categories.get(0));
    }

    private Product createSampleProduct(String name, double price, String desc, String image,
            Category category, boolean isPromotion, int discountPercent) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(desc);
        product.setImage(image);
        product.setCategory(category);
        product.setPromotionType(isPromotion ? "DISCOUNT" : "NONE");
        product.setDiscountPercent(discountPercent);
        product.setGiftDescription("");
        product.setStockQuantity(50);
        return product;
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
