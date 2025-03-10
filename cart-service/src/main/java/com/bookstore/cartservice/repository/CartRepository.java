package com.bookstore.cartservice.repository;

import com.bookstore.cartservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(String userId);
    CartItem findByUserIdAndBookIsbn(String userId, String bookIsbn);

    void deleteByUserIdAndBookIsbn(String userId, String bookIsbn);
}