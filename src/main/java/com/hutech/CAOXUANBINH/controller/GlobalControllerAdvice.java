package com.hutech.CAOXUANBINH.controller;

import com.hutech.CAOXUANBINH.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import com.hutech.CAOXUANBINH.model.Category;

/**
 * Tự động inject danh mục vào tất cả view (layout.html dùng sec:authorize)
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CategoryService categoryService;
    private final com.hutech.CAOXUANBINH.service.CartService cartService;

    @ModelAttribute("rootCategories")
    public List<Category> rootCategories() {
        return categoryService.getRootCategories();
    }

    @ModelAttribute("cartCount")
    public int cartCount() {
        return cartService.getCartCount();
    }
}
