package com.example.demo.controller;

import com.example.demo.entity.Comment;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// 评论控制器
@RestController
@RequestMapping("/comments")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    // 获取所有评论
    @GetMapping
    public List<Comment> getAllComments() {
        logger.info("收到获取所有评论的请求");
        return commentService.getAllComments();
    }

    // 插入新的评论
    @PostMapping
    public void insertComment(@RequestBody Comment comment) {
        logger.info("接收到的评论对象: {}", comment);
        if (comment.getNewsId() == null) {
            logger.error("新闻ID不能为空");
            throw new IllegalArgumentException("新闻ID不能为空");
        }
        logger.info("收到插入评论的请求: {}", comment);
        commentService.insertComment(comment);
    }

    // 根据ID删除评论
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        logger.info("收到删除评论的请求，ID: {}", id);
        commentService.deleteComment(id);
    }

    // 根据新闻ID获取评论
    @GetMapping("/news/{newsId}")
    public List<Comment> getCommentsByNewsId(@PathVariable Long newsId) {
        logger.info("收到根据新闻ID获取评论的请求，newsId: {}", newsId);
        return commentService.getCommentsByNewsId(newsId);
    }
}
