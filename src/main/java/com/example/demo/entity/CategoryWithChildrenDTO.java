package com.example.demo.entity;

import java.util.List;

public class CategoryWithChildrenDTO {
    private Long cateId; // 分类ID
    private String cateName; // 分类名称
    private boolean isHaveChild; // 是否有子分类
    private List<ChildCategoryDTO> children; // 子分类列表

    // Getters and setters
    public Long getCateId() {
        return cateId;
    }

    public void setCateId(Long cateId) {
        this.cateId = cateId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public boolean isHaveChild() {
        return isHaveChild;
    }

    public void setHaveChild(boolean haveChild) {
        isHaveChild = haveChild;
    }

    public List<ChildCategoryDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ChildCategoryDTO> children) {
        this.children = children;
    }
}

class ChildCategoryDTO {
    private Long childId; // 子分类ID
    private String name; // 子分类名称
    private String images; // 子分类图片，存储为JSON字符串

    // Getters and setters
    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
