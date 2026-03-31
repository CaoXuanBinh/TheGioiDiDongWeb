package com.hutech.CAOXUANBINH.controller;

import com.hutech.CAOXUANBINH.model.Product;
import com.hutech.CAOXUANBINH.service.CategoryService;
import com.hutech.CAOXUANBINH.service.ProductService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String listProducts(@RequestParam(value = "categoryId", required = false) Long categoryId,
            Model model) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        model.addAttribute("products", products);
        model.addAttribute("promotionProducts", productService.getPromotionProducts());
        model.addAttribute("selectedCategoryId", categoryId);
        return "products/product-list";
    }

    @GetMapping("/detail/{id}")
    public String viewProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        return "products/product-detail";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/add-product";
    }

    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult result,
            @RequestParam("imageFile") MultipartFile imageFile, Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "products/add-product";
        }

        try {
            productService.addProduct(product, imageFile);
            redirectAttributes.addFlashAttribute("message", "Add product success.");
        } catch (IOException e) {
            model.addAttribute("categories", categoryService.getAllCategories());
            redirectAttributes.addFlashAttribute("error", "Cannot add product. Please try again.");
            return "redirect:/products/add";
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/add-product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid Product product, BindingResult result,
            @RequestParam("imageFile") MultipartFile imageFile, Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            product.setId(id);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "products/add-product";
        }

        try {
            productService.updateProduct(product, imageFile);
            redirectAttributes.addFlashAttribute("message", "Update product success.");
        } catch (IOException e) {
            product.setId(id);
            model.addAttribute("categories", categoryService.getAllCategories());
            redirectAttributes.addFlashAttribute("error", "Cannot update product. Please try again.");
            return "redirect:/products/edit/" + id;
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id,
            RedirectAttributes redirectAttrs) {
        try {
            productService.deleteProductById(id);
            redirectAttrs.addFlashAttribute("message", "Da xoa san pham thanh cong!");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            redirectAttrs.addFlashAttribute("error", "Khong the xoa san pham nay vi da co trong don mua hang!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Co loi xay ra khi xoa san pham: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }
}
