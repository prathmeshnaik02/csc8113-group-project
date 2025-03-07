package com.bookstore.cartservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users") //  确保表名匹配数据库
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // 角色字段

    //  需要无参构造函数（JPA 需要）
    public User() {}

    //  关键修改：添加 ID 作为参数的构造函数
    public User(Long id) {
        this.id = id;
    }

    //  修正 `setRole()` 逻辑，避免重复添加 `ROLE_`
    public void setRole(String role) {
        if (!role.startsWith("ROLE_")) {
            this.role = "ROLE_" + role;
        } else {
            this.role = role;
        }
    }
}



