package com.wrf.dao.impl;

import com.wrf.AppConfig;
import com.wrf.dao.BookDao;
import com.wrf.Bean.Book;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;

class BookDaoImplTest {

    BookDao bookDao;
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.bookDao  = context.getBean(BookDaoImpl.class);
    }
    @Test
    void addBook() {
        Book book = new Book(null, "testbook1", "wrf", new BigDecimal(1222), 1334, 9999, null);
        bookDao.addBook(book);
    }

    @Test
    void deleteBookById() {
        bookDao.deleteBookById(21);
    }

    @Test
    void updateBook() {
        int i = bookDao.updateBook(new Book(22, "testbook1", "wrf111", new BigDecimal(1222), 1334, 9999, null));
        System.out.println(i);
    }

    @Test
    void queryBookByID() {
        System.out.println(bookDao.queryBookByID(22));
    }

    @Test
    void queryBooks() {
        for(Book v : bookDao.queryBooks())
            System.out.println(v);
    }
}