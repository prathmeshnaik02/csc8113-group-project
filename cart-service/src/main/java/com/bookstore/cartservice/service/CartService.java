package com.bookstore.cartservice.service;

import com.bookstore.cartservice.model.CartItem;
import com.bookstore.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final RestTemplate restTemplate;
    private final String catalogUrl = "http://catalog-service:8000/books/";

    public CartItem addToCart(String userId, String bookIsbn, int quantity) {
        // Verify book exists
        restTemplate.getForObject(catalogUrl + bookIsbn, Object.class);

        CartItem existingItem = cartRepository.findByUserIdAndBookIsbn(userId, bookIsbn);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            return cartRepository.save(existingItem);
        }

        CartItem newItem = new CartItem();
        newItem.setUserId(userId);
        newItem.setBookIsbn(bookIsbn);
        newItem.setQuantity(1);
        return cartRepository.save(newItem);
    }

    // delete method
    public void removeFromCart(String userId, String bookIsbn) {
        CartItem item = cartRepository.findByUserIdAndBookIsbn(userId, bookIsbn);
        if (item != null) {
            cartRepository.delete(item);
        }
    }

    public List<CartItem> getCart(String userId) {
        return cartRepository.findByUserId(userId);
    }
}