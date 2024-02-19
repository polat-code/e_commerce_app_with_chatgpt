package com.example.ecommerce_app_with_chathpt.controller;

import com.example.ecommerce_app_with_chathpt.model.Category;
import com.example.ecommerce_app_with_chathpt.model.request.CategoryRequest;
import com.example.ecommerce_app_with_chathpt.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping("")
    public List<Category> getAll(){
        return categoryService.getAll();

    }

    @PostMapping("")
    public void saveAllCategory(@RequestBody List<CategoryRequest> categoryList)
    {
        categoryService.saveAllCategory(categoryList);
    }
}
