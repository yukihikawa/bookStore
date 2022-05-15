package com.wrf.dao;

import com.wrf.pojo.Book;

import java.util.List;

/**
 * @program: bookStore
 * @description: 图书模块DAO接口
 * @author: Rifu Wu
 * @create: 2022-05-15 00:01
 **/
public interface BookDao {
    public int addBook(Book book);

    public int deleteBookById(Integer id);

    public int updateBook(Book book);

    public Book queryBookByID(Integer id);

    public List<Book> queryBooks();

}
