package com.example.demo.mapper;

import com.example.demo.entity.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NewsMapper {
    @Select("SELECT * FROM news")
    @Results(id = "NewsResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "content", column = "content"),
            @Result(property = "type", column = "type"),
            @Result(property = "imageUrls", column = "imageUrls", typeHandler = com.example.demo.handler.JsonTypeHandler.class)
    })
    List<News> getAllNews();

    @Select("SELECT * FROM news WHERE id = #{id}")
    @ResultMap("NewsResultMap")
    News getNewsById(Long id);

    @Insert("INSERT INTO news(title, content, type, imageUrls) VALUES(#{title}, #{content}, #{type}, #{imageUrls, typeHandler=com.example.demo.handler.JsonTypeHandler})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertNews(News news);

    @Delete("DELETE FROM news WHERE id = #{id}")
    void deleteNews(Long id);

    @Update("UPDATE news SET title = #{title}, content = #{content}, type = #{type}, imageUrls = #{imageUrls, typeHandler=com.example.demo.handler.JsonTypeHandler} WHERE id = #{id}")
    void updateNews(News news);
}
