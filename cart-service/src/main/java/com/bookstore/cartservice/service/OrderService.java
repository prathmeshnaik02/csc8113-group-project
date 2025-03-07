package com.bookstore.cartservice.service;

import com.bookstore.cartservice.model.*;
import com.bookstore.cartservice.repository.OrderRepository;
import com.bookstore.cartservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookService bookService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, BookService bookService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.bookService = bookService;
    }

    @Transactional
    public Order createOrder(Long userId, List<CartItem> cartItems) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING); //  使用 `enum` 代替 `"PENDING"`
        order.setCreatedAt(LocalDateTime.now());

        double total = 0;

        try {
            for (CartItem cartItem : cartItems) {
                Book book = bookService.getBookById(cartItem.getBook().getId())
                        .orElseThrow(() -> new RuntimeException("Book not found"));

                if (book.getStock() < cartItem.getQuantity()) {
                    throw new RuntimeException("库存不足：" + book.getTitle());
                }

                // 更新库存
                bookService.reduceStock(book.getId(), cartItem.getQuantity());

                // 计算订单总价
                total += cartItem.getQuantity() * book.getPrice();
            }

            order.setTotalPrice(total);
            return orderRepository.save(order);

        } catch (Exception e) {
            // 订单创建失败时恢复库存
            for (CartItem cartItem : cartItems) {
                bookService.increaseStock(cartItem.getBook().getId(), cartItem.getQuantity());
            }

            logger.error("订单创建失败，库存已回滚，用户ID: {}", userId, e);
            throw new RuntimeException("订单创建失败", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}



