package com.example.demo.service;

import com.example.demo.entity.Comment;

import java.util.List;

// 评论服务接口
public interface CommentService {
    List<Comment> getAllComments();  // 获取所有评论
    void insertComment(Comment comment);  // 插入新的评论
    void deleteComment(Long id);  // 根据ID删除评论
    List<Comment> getCommentsByNewsId(Long newsId);  // 根据新闻ID获取评论
}
