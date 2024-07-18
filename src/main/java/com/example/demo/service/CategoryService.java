package com.example.demo.service;

import com.example.demo.entity.CategoryWithChildrenDTO;
import com.example.demo.entity.CategoryProductDTO;
import com.example.demo.entity.Category;
import com.example.demo.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> getAllRootCategories() {
        return categoryMapper.findAllRootCategories();
    }

    public List<Category> getSubCategories(Long parentId) {
        return categoryMapper.findSubCategories(parentId);
    }

    public Optional<Category> getCategoryById(Long id) {
        return Optional.ofNullable(categoryMapper.findById(id));
    }

    public Optional<Category> getCategoryByName(String name) {
        return Optional.ofNullable(categoryMapper.findByName(name));
    }

    public void saveCategory(Category category) {
        if (category.getId() == null) {
            categoryMapper.insert(category);
        } else {
            categoryMapper.update(category);
        }
    }

    public void deleteCategory(Long id) {
        categoryMapper.delete(id);
    }

    public List<CategoryProductDTO> getCategoriesWithProducts() {
        return categoryMapper.findCategoriesWithProducts();
    }

    public List<CategoryWithChildrenDTO> getCategoriesWithChildren() {
        return categoryMapper.findCategoriesWithChildren();
    }
}
