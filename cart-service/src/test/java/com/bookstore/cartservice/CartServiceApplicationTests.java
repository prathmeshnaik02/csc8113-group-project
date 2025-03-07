package com.bookstore.cartservice;

import com.bookstore.cartservice.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CartServiceApplicationTests {

    // ✅ 解决 `@MockBean` 过时问题，改用 `@Mock`
    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void contextLoads() {
        // 确保 Spring Boot 应用上下文可以正确加载
    }
}


