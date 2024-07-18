package com.example.demo.service.impl;

import com.example.demo.entity.CartItem;
import com.example.demo.mapper.CartItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartItemMapper cartItemMapper;

    public List<CartItem> getCartItemsByUserId(Long userId) {
        return cartItemMapper.findByUserId(userId);
    }

    public void saveCartItem(CartItem cartItem) {
        if (cartItem.getId() == null) {
            cartItemMapper.insert(cartItem);
        } else {
            cartItemMapper.update(cartItem);
        }
    }

    public void deleteCartItem(Long id) {
        cartItemMapper.delete(id);
    }
}
