package com.bookstore.cartservice.repository;

import com.bookstore.cartservice.model.Book;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Lock(LockModeType.OPTIMISTIC) //  使用乐观锁
    @Query("SELECT b FROM Book b WHERE b.id = :id") //  JPQL 查询（修正 `b.bid` 为 `b.id`）
    Optional<Book> findByIdWithLock(@Param("id") Long id);
}




