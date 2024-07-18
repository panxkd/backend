package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            System.out.println("接收到订单数据: " + order); // 添加中文日志记录
            orderService.saveOrder(order);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("创建订单失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            Order updatedOrder = order.get();
            updatedOrder.setUserId(orderDetails.getUserId());
            updatedOrder.setOrderDate(orderDetails.getOrderDate());
            updatedOrder.setTotalAmount(orderDetails.getTotalAmount());
            updatedOrder.setUserName(orderDetails.getUserName());
            updatedOrder.setAddress(orderDetails.getAddress());
            updatedOrder.setPhone(orderDetails.getPhone());
            updatedOrder.setOrderItems(orderDetails.getOrderItems());
            orderService.saveOrder(updatedOrder);
            return ResponseEntity.ok(updatedOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}


