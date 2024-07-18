package com.example.demo.mapper;

import com.example.demo.entity.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    @Select("SELECT * FROM order_items WHERE order_id = #{orderId}")
    @Results(id = "OrderItemResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "productName", column = "product_name"),
            @Result(property = "productPrice", column = "product_price"),
            @Result(property = "productQuantity", column = "product_quantity"),
            @Result(property = "itemTotal", column = "item_total")
    })
    List<OrderItem> getOrderItemsByOrderId(Long orderId);

    @Insert("INSERT INTO order_items (order_id, product_name, product_price, product_quantity, item_total) VALUES (#{orderId}, #{productName}, #{productPrice}, #{productQuantity}, #{itemTotal})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(OrderItem orderItem);

    @Delete("DELETE FROM order_items WHERE order_id = #{orderId}")
    void deleteByOrderId(Long orderId);
}
