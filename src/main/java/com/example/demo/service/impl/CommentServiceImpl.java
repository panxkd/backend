package com.example.demo.service.impl;

import com.example.demo.entity.Comment;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// 评论服务实现类
@Service
public class CommentServiceImpl implements CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getAllComments() {
        logger.info("获取所有评论");
        return commentMapper.getAllComments();
    }

    @Override
    public void insertComment(Comment comment) {
        logger.info("插入评论: {}", comment);
        commentMapper.insertComment(comment);
    }

    @Override
    public void deleteComment(Long id) {
        logger.info("删除评论，ID: {}", id);
        commentMapper.deleteComment(id);
    }

    @Override
    public List<Comment> getCommentsByNewsId(Long newsId) {
        logger.info("根据新闻ID获取评论，newsId: {}", newsId);
        return commentMapper.getCommentsByNewsId(newsId);
    }
}
