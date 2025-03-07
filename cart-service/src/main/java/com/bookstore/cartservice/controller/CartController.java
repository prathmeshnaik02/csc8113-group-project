package com.bookstore.cartservice.controller;

import com.bookstore.cartservice.model.Cart;
import com.bookstore.cartservice.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestParam Long bookId, @RequestParam int quantity) {
        String username = userDetails.getUsername();
        Long userId = cartService.getUserIdByUsername(username);

        try {
            Cart updatedCart = cartService.addItemToCart(userId, bookId, quantity);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("添加商品失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("购物车已清空");
    }
}





