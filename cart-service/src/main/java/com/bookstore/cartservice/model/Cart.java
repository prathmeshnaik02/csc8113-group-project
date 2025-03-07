package com.bookstore.cartservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    // ✅ 确保 Redis 反序列化时能创建对象
    public Cart() {
        this.items = new ArrayList<>();
    }

    // ✅ 手动添加 `getItems()`，防止 Redis 反序列化问题
    public List<CartItem> getItems() {
        return items == null ? new ArrayList<>() : items;
    }

    @PreRemove
    public void clearCartItems() {
        if (items != null) {
            items.clear();
        }
    }
}






