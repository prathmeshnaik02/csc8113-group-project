package com.bookstore.cartservice.service;

import com.bookstore.cartservice.model.Cart;
import com.bookstore.cartservice.model.CartItem;
import com.bookstore.cartservice.model.User;
import com.bookstore.cartservice.repository.CartRepository;
import com.bookstore.cartservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Cart> cartRedisTemplate; // 确保 RedisTemplate<String, Cart> 依赖正确注入

    public CartService(CartRepository cartRepository, UserRepository userRepository,
                       RedisTemplate<String, Cart> cartRedisTemplate) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartRedisTemplate = cartRedisTemplate;
    }

    /**
     * 获取用户购物车（优先查询 Redis 缓存）
     */
    public Cart getCartByUserId(Long userId) {
        String cacheKey = "cart:" + userId;

        // **先从 Redis 获取购物车**
        Cart cachedCart = cartRedisTemplate.opsForValue().get(cacheKey);
        if (cachedCart != null) {
            logger.info("✅ 从 Redis 获取购物车成功: userId={}", userId);
            return cachedCart;
        }

        // **如果 Redis 没有，则查询数据库**
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(new User(userId));
                    return cartRepository.save(newCart);
                });

        // **存入 Redis，缓存 10 分钟**
        cartRedisTemplate.opsForValue().set(cacheKey, cart, Duration.ofMinutes(10));
        logger.info("🛒 购物车存入 Redis 缓存: userId={}, 过期时间=10分钟", userId);

        return cart;
    }

    /**
     * 添加商品到购物车（自动清除 Redis 缓存）
     */
    @Transactional
    public Cart addItemToCart(Long userId, Long bookId, int quantity) {
        Cart cart = getCartByUserId(userId);

        // **确保购物车项列表非空**
        List<CartItem> items = cart.getItems();
        if (items == null) {
            items = new ArrayList<>();
            cart.setItems(items);
        }

        // **查找购物车中是否已有该商品**
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getBookId().equals(bookId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
            logger.info("🛒 购物车已有该商品，更新数量: userId={}, bookId={}, 新数量={}",
                    userId, bookId, existingItem.get().getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setBookId(bookId);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            items.add(newItem);
            logger.info("🛒 购物车新增商品: userId={}, bookId={}, 数量={}", userId, bookId, quantity);
        }

        // **数据库保存**
        cartRepository.save(cart);

        // **清理 Redis 缓存**
        cartRedisTemplate.delete("cart:" + userId);
        logger.info("🗑 购物车更新，清理 Redis 缓存: userId={}", userId);

        return cart;
    }

    /**
     * 清空购物车（自动清除 Redis 缓存）
     */
    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);

        // **确保 Redis 删除同步**
        boolean deleted = Boolean.TRUE.equals(cartRedisTemplate.delete("cart:" + userId));
        logger.info("🗑 购物车清空，删除 Redis 缓存: userId={}, Redis缓存删除成功={}", userId, deleted);
    }

    /**
     * 根据用户名获取用户 ID
     */
    public Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("❌ 用户未找到: " + username));
    }
}















