package com.example.demo.entity;

import java.util.List;

// 新闻实体类
public class News {
    private Long id;  // 新闻ID
    private String title;  // 新闻标题
    private String content;  // 新闻内容
    private String type; // 新闻类型（纯文字，单图，三图）
    private List<String> imageUrls; // 图片URL列表

    // Getters 和 Setters 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
