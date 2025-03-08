package com.bookstore.cartservice.controller;

import com.bookstore.cartservice.model.User;
import com.bookstore.cartservice.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 用户注册 API
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User userRequest) {
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("{\"error\": \"Username already exists\"}");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword())); // 确保密码加密存储
        user.setRole("ROLE_USER"); // 默认赋予 USER 角色

        userRepository.save(user);
        return ResponseEntity.ok("{\"message\": \"User registered successfully\"}");
    }
}



