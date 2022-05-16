package com.wrf.dao.impl;

import com.wrf.Bean.Page;
import com.wrf.dao.BaseDao;
import com.wrf.dao.BookDao;
import com.wrf.Bean.Book;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: bookStore
 * @description:
 * @author: Rifu Wu
 * @create: 2022-05-15 00:20
 **/

@Component
public class BookDaoImpl extends BaseDao implements BookDao {

    //添加一本书
    @Override
    public int addBook(Book book) {
        String sql = "INSERT into t_book(`name`, `author`, `price`, `sales`, `stock`, img_path) VALUES(?, ?, ?, ?, ?, ?)";
        String sql1 = "INSERT into t_book(`name`,`author`,`price`,`sales`,`stock`,`img_path`) values(?,?,?,?,?,?)";
        return update(sql, book.getName(), book.getAuthor(), book.getPrice(), book.getSales(), book.getStock(), book.getImgPath());
    }


    //根据ID删除一本书
    @Override
    public int deleteBookById(Integer id) {
        String sql = "DELETE FROM t_book WHERE id = ?";
        return update(sql, id);
    }

    //更新书籍信息
    @Override
    public int updateBook(Book book) {
        String sql = "UPDATE t_book SET `name`=?,`author`=?,`price`=?,`sales`=?,`stock`=?,`img_path`=? WHERE id = ?";
        return update(sql, book.getName(), book.getAuthor(), book.getPrice(), book.getSales(), book.getStock(), book.getImgPath(), book.getId());
    }

    //根据ID查询一本书
    @Override
    public Book queryBookByID(Integer id) {
        String sql = "SELECT `id` , `name` , `author` , `price` , `sales` , `stock` , `img_path` imgPath FROM t_book WHERE id = ?";
        return queryForOne(Book.class, sql, id);
    }

    //获取所有书的列表
    @Override
    public List<Book> queryBooks() {
        String sql = "SELECT `id` , `name` , `author` , `price` , `sales` , `stock` , `img_path` imgPath FROM t_book";
        return queryForList(Book.class, sql);
    }

    //查询总条数
    @Override
    public Integer queryForPageTotalCount() {
        String sql = "SELECT COUNT(*) FROM t_book";
        Number count =(Number) queryForSingleValue(sql);
        return count.intValue();
    }

    //单页元素, 按照首位查询一页上的所有book
    @Override
    public List<Book> queryForPageElement(int begin, int pageSize) {
        String sql = "SELECT `id` , `name` , `author` , `price` , `sales` , `stock` , `img_path` imgPath FROM t_book LIMIT ?, ?";
        return queryForList(Book.class,sql,begin,pageSize);
    }



}