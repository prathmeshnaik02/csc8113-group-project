package com.bookstore.cartservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book")
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private double price;

    @Version  // 添加乐观锁
    private Integer version;

    @Column(nullable = false)
    private int stock;

    public void reduceStock(int quantity) {
        if (stock < quantity) {
            throw new RuntimeException("库存不足");
        }
        stock -= quantity;
    }
}



