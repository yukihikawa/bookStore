package com.wrf.service.impl;

import com.wrf.dao.BookDao;
import com.wrf.dao.impl.BookDaoImpl;
import com.wrf.pojo.Book;
import com.wrf.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: bookStore
 * @description: BookService实现类
 * @author: Rifu Wu
 * @create: 2022-05-15 17:22
 **/

@Component
public class BookServiceImpl implements BookService {

    final
    BookDaoImpl bookDao;

    public BookServiceImpl(BookDaoImpl bookdao) {
        this.bookDao = bookdao;
    }

    @Override
    public void addBook(Book book) {
        bookDao.addBook(book);
    }

    @Override
    public void deleteBook(String bookId) {
        bookDao.deleteBookById(Integer.valueOf(bookId));
    }

    @Override
    public void updateBook(Book book) {
        bookDao.updateBook(book);
    }

    @Override
    public Book getById(String bookId) {
        return bookDao.queryBookByID(Integer.valueOf(bookId));
    }

    @Override
    public List<Book> getBookList() {
        return bookDao.queryBooks();
    }
}