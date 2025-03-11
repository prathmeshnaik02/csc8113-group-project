package com.bookstore.cartservice.controller;

import com.bookstore.cartservice.model.CartItem;
import com.bookstore.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookstore.cartservice.dto.AddToCartRequest;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    // GET method
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping
    public ResponseEntity<CartItem> addToCart(@RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(
                cartService.addToCart(
                        request.getUserId(),
                        request.getBookIsbn(),
                        request.getQuantity()
                )
        );
    }

    // DELETE endpoint
    @DeleteMapping("/{userId}/{bookIsbn}")
    public ResponseEntity<Void> removeFromCart(
            @PathVariable String userId,
            @PathVariable String bookIsbn) {
        cartService.removeFromCart(userId, bookIsbn);
        return ResponseEntity.noContent().build();
    }

    // health API endpoint
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        return ResponseEntity.ok(response);
    }
}
