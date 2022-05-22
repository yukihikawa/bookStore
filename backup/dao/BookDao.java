package com.wrf.dao;

import com.wrf.Bean.Book;

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

    public Integer queryForPageTotalCount();

    Integer queryForPageTotalCount(int min, int max);

    public List<Book> queryForPageElement(int begin, int pageSize);

    List<Book> queryForPageElement(int begin, int pageSize, int min, int max);
}
