package com.example.demo.mapper;

import com.example.demo.entity.CategoryWithChildrenDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.CategoryProductDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("SELECT * FROM categories WHERE parent_id IS NULL")
    List<Category> findAllRootCategories();

    @Select("SELECT * FROM categories WHERE parent_id = #{parentId}")
    List<Category> findSubCategories(Long parentId);

    @Select("SELECT * FROM categories WHERE id = #{id}")
    Category findById(Long id);

    @Select("SELECT * FROM categories WHERE name = #{name}")
    Category findByName(String name);

    @Insert("INSERT INTO categories(name, parent_id, images) VALUES(#{name}, #{parentId}, #{images})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Category category);

    @Update("UPDATE categories SET name=#{name}, parent_id=#{parentId}, images=#{images} WHERE id = #{id}")
    void update(Category category);

    @Delete("DELETE FROM categories WHERE id = #{id}")
    void delete(Long id);

    @Select("SELECT " +
            "c1.id AS cateId, " +
            "c1.name AS cateName, " +
            "CASE WHEN COUNT(c2.id) > 0 THEN TRUE ELSE FALSE END AS isHaveChild, " +
            "JSON_ARRAYAGG(JSON_OBJECT('childId', c2.id, 'name', c2.name, 'images', c2.images)) AS children " +
            "FROM categories c1 " +
            "LEFT JOIN categories c2 ON c1.id = c2.parent_id " +
            "GROUP BY c1.id, c1.name")
    List<CategoryWithChildrenDTO> findCategoriesWithChildren();

    @Select("SELECT " +
            "c.id AS categoryId, " +
            "c.name AS categoryName, " +
            "p.id AS productId, " +
            "p.name AS productName, " +
            "p.price AS productPrice, " +
            "p.description AS productDescription " +
            "FROM categories c " +
            "LEFT JOIN products p ON c.id = p.category_id")
    List<CategoryProductDTO> findCategoriesWithProducts();
}
