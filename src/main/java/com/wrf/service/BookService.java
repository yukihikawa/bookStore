package com.wrf.service;

import com.wrf.pojo.Book;

import java.util.List;

/**
 * @program: bookStore
 * @description: bookservice接口
 * @author: Rifu Wu
 * @create: 2022-05-15 17:11
 **/
public interface BookService {
    //向数据库插入一本书
    public void addBook(Book book);

    //删除一本书
    public void deleteBook(String bookId);

    //更新一本书
    public void updateBook(Book book);

    //根据ID查询一本书
    public Book getById(String bookId);

    //获取所有图书
    public List<Book> getBookList();

}
