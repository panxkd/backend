package com.example.demo.mapper;

import com.example.demo.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("SELECT * FROM orders")
    @Results(id = "OrderResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "orderDate", column = "order_date"),
            @Result(property = "totalAmount", column = "total_amount"),
            @Result(property = "userName", column = "user_name"),
            @Result(property = "address", column = "address"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "orderItems", column = "id", many = @Many(select = "com.example.demo.mapper.OrderItemMapper.getOrderItemsByOrderId"))
    })
    List<Order> findAll();

    @Select("SELECT * FROM orders WHERE id = #{id}")
    @ResultMap("OrderResultMap")
    Order findById(Long id);

    @Insert("INSERT INTO orders (user_id, order_date, total_amount, user_name, address, phone) VALUES (#{userId}, #{orderDate}, #{totalAmount}, #{userName}, #{address}, #{phone})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Order order);

    @Update("UPDATE orders SET user_id = #{userId}, order_date = #{orderDate}, total_amount = #{totalAmount}, user_name = #{userName}, address = #{address}, phone = #{phone} WHERE id = #{id}")
    void update(Order order);

    @Delete("DELETE FROM orders WHERE id = #{id}")
    void delete(Long id);
}
