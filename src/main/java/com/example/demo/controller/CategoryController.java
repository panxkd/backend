package com.example.demo.controller;

import com.example.demo.entity.CategoryWithChildrenDTO;
import com.example.demo.entity.CategoryProductDTO;
import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllRootCategories() {
        return categoryService.getAllRootCategories();
    }

    @GetMapping("/{id}/subcategories")
    public List<Category> getSubCategories(@PathVariable Long id) {
        return categoryService.getSubCategories(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
        Optional<Category> category = categoryService.getCategoryByName(name);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        categoryService.saveCategory(category);
        return category;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            Category updatedCategory = category.get();
            updatedCategory.setName(categoryDetails.getName());
            updatedCategory.setParentId(categoryDetails.getParentId());
            updatedCategory.setImages(categoryDetails.getImages());
            categoryService.saveCategory(updatedCategory);
            return ResponseEntity.ok(updatedCategory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/with-children")
    public List<CategoryWithChildrenDTO> getCategoriesWithChildren() {
        return categoryService.getCategoriesWithChildren();
    }

    @GetMapping("/with-products")
    public List<CategoryProductDTO> getCategoriesWithProducts() {
        return categoryService.getCategoriesWithProducts();
    }
}
