package com.bookstore.cartservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;


    @Getter
    @ManyToOne(fetch = FetchType.LAZY) // ✅ 关键：关联 `Book`
    @JoinColumn(name = "book_id", nullable = false, insertable = false, updatable = false)
    private Book book;


    @Setter
    @Getter
    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private int quantity;

    //  修正 `setBook()`
    public void setBook(Book book) {
        this.book = book;
        if (book != null) {
            this.bookId = book.getId(); // 确保 `bookId` 与 `Book` 对象同步
        }
    }
}







