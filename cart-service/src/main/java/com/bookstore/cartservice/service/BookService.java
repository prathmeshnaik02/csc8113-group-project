package com.bookstore.cartservice.service;

import com.bookstore.cartservice.model.Book;
import com.bookstore.cartservice.repository.BookRepository;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * 获取所有书籍列表
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * 根据 ID 获取书籍
     */
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * 添加书籍
     */
    @Transactional
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * 删除书籍
     */
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found");
        }
        bookRepository.deleteById(id);
    }

    /**
     * **减少库存**
     * - **使用乐观锁（@Version）**
     * - **自动重试 3 次，防止并发失败**
     * - **增加短暂等待，避免数据库高频冲突**
     */
    public void reduceStock(Long bookId, int quantity) {
        for (int i = 0; i < 3; i++) { // ⏳ 自动重试 3 次，减少失败概率
            try {
                transactionalReduceStock(bookId, quantity); //  每次调用都更新事务
                return; //  成功后退出循环
            } catch (OptimisticLockException e) {
                logger.warn(" 库存扣减失败，尝试重试 {}/3 次, bookId={}", i + 1, bookId);
                try {
                    Thread.sleep(50); //  等待 50ms，减少数据库冲突
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new RuntimeException(" 库存更新失败，重试次数已达上限");
    }

    /**
     * **使用 `@Transactional` 进行库存扣减（保证事务）**
     */
    @Transactional
    public void transactionalReduceStock(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId) //  确保使用乐观锁
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getStock() < quantity) {
            throw new RuntimeException("库存不足: " + book.getTitle());
        }

        book.setStock(book.getStock() - quantity);
        bookRepository.save(book);

        logger.info(" 库存减少成功: 书籍ID={}, 扣减数量={}", bookId, quantity);
    }

    /**
     * **增加库存，使用 `@Version` 处理并发问题**
     */
    @Transactional
    public void increaseStock(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId) //  确保使用乐观锁
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setStock(book.getStock() + quantity);
        bookRepository.save(book);

        logger.info(" 库存增加成功: 书籍ID={}, 增加数量={}", bookId, quantity);
    }

    /**
     * **检查库存是否充足**
     */
    public boolean isStockAvailable(Long bookId, int quantity) {
        return bookRepository.findById(bookId)
                .map(book -> book.getStock() >= quantity)
                .orElse(false);
    }
}











