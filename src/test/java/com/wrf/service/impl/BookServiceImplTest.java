package com.wrf.service.impl;

import com.wrf.AppConfig;
import com.wrf.Bean.Book;
import com.wrf.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;


class BookServiceImplTest {

    BookService bookService;

    {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.bookService  = context.getBean(BookService.class);

    }

    @Test
    void saveBook() {
        Book book = new Book(null, "testbook1", "wrf", new BigDecimal(1222), 1334, 9999, null);
        bookService.addBook(book);
    }

    @Test
    void deleteBook() {
        bookService.deleteBook("24");
    }

    @Test
    void updateBook() {
        Book book = new Book(23, "testbook1", "wrf", new BigDecimal(1222), 1334, 9999, "aawds");
        bookService.updateBook(book);
    }

    @Test
    void getById() {
        System.out.println(bookService.getById("5"));
    }

    @Test
    void getBookList() {
        for (Book b : bookService.getBookList())
            System.out.println(b);
    }
}