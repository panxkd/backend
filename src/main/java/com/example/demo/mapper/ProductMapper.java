package com.example.demo.mapper;

import com.example.demo.entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("SELECT id, category_id AS categoryId, name, description, price, images, type FROM products")
    List<Product> findAll();

    @Select("SELECT id, category_id AS categoryId, name, description, price, images, type FROM products WHERE id = #{id}")
    Product findById(Long id);

    @Select("SELECT id, category_id AS categoryId, name, description, price, images, type FROM products WHERE category_id IN (SELECT id FROM categories WHERE name = #{categoryName})")
    List<Product> findByCategoryName(String categoryName);

    @Insert("INSERT INTO products(id, category_id, name, description, price, images, type) VALUES(#{id}, #{categoryId}, #{name}, #{description}, #{price}, #{images}, #{type})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Product product);

    @Update("UPDATE products SET category_id=#{categoryId}, name=#{name}, description=#{description}, price=#{price}, images=#{images}, type=#{type} WHERE id=#{id}")
    void update(Product product);

    @Delete("DELETE FROM products WHERE id=#{id}")
    void delete(Long id);
}
