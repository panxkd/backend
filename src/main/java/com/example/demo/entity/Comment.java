package com.example.demo.entity;

// 评论实体类
public class Comment {
    private Long id;  // 评论ID
    private Long newsId;  // 所属新闻ID
    private String content;  // 评论内容
    private String author;  // 评论作者

    // Getters 和 setters 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
