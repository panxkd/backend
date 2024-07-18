package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.mapper.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    public List<Order> getAllOrders() {
        return orderMapper.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return Optional.ofNullable(orderMapper.findById(id));
    }

    @Transactional
    public void saveOrder(Order order) {
        if (order.getId() == null) {
            orderMapper.insert(order);
            for (OrderItem item : order.getOrderItems()) {
                item.setOrderId(order.getId());
                orderItemMapper.insert(item);
            }
        } else {
            orderMapper.update(order);
            orderItemMapper.deleteByOrderId(order.getId());
            for (OrderItem item : order.getOrderItems()) {
                item.setOrderId(order.getId());
                orderItemMapper.insert(item);
            }
        }
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderItemMapper.deleteByOrderId(id);
        orderMapper.delete(id);
    }
}
