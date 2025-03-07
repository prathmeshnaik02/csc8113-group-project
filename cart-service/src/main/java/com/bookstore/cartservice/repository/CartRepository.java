package com.bookstore.cartservice.repository;

import com.bookstore.cartservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);

    @Transactional
    void deleteByUserId(Long userId); //  修复 `deleteByUserId()`
}
