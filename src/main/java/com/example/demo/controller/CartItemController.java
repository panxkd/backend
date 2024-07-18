package com.example.demo.controller;

import com.example.demo.entity.CartItem;
import com.example.demo.service.impl.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/user/{userId}")
    public List<CartItem> getCartItemsByUserId(@PathVariable Long userId) {
        return cartItemService.getCartItemsByUserId(userId);
    }

    @PostMapping
    public CartItem createCartItem(@RequestBody CartItem cartItem) {
        cartItemService.saveCartItem(cartItem);
        return cartItem;
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long id, @RequestBody CartItem cartItemDetails) {
        cartItemService.saveCartItem(cartItemDetails);
        return ResponseEntity.ok(cartItemDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.noContent().build();
    }
}
