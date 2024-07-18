package com.example.demo.mapper;

import com.example.demo.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    // 获取所有评论
    @Select("SELECT * FROM comment")
    List<Comment> getAllComments();

    // 插入新的评论
    @Insert("INSERT INTO comment(news_id, content, author) VALUES(#{newsId}, #{content}, #{author})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertComment(Comment comment);

    // 根据ID删除评论
    @Delete("DELETE FROM comment WHERE id = #{id}")
    void deleteComment(Long id);

    // 根据新闻ID获取评论
    @Select("SELECT * FROM comment WHERE news_id = #{newsId}")
    List<Comment> getCommentsByNewsId(Long newsId);
}
