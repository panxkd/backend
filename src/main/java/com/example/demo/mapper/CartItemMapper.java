package com.example.demo.mapper;

import com.example.demo.entity.CartItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartItemMapper {

    @Select("SELECT * FROM cart_items WHERE user_id = #{userId}")
    List<CartItem> findByUserId(Long userId);

    @Insert("INSERT INTO cart_items(user_id, product_id, quantity) VALUES(#{userId}, #{productId}, #{quantity})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(CartItem cartItem);

    @Update("UPDATE cart_items SET quantity=#{quantity} WHERE id = #{id}")
    void update(CartItem cartItem);

    @Delete("DELETE FROM cart_items WHERE id = #{id}")
    void delete(Long id);
}
