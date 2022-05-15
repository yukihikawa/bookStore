package com.wrf.dao.impl;

import com.wrf.dao.BaseDao;
import com.wrf.dao.BookDao;
import com.wrf.pojo.Book;
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
    @Override
    public int addBook(Book book) {
        String sql = "INSERT into t_book(`name`, `author`, `price`, `sales`, `stock`, img_path) VALUES(?, ?, ?, ?, ?, ?)";
        String sql1 = "INSERT into t_book(`name`,`author`,`price`,`sales`,`stock`,`img_path`) values(?,?,?,?,?,?)";
        return update(sql, book.getName(), book.getAuthor(), book.getPrice(), book.getSales(), book.getStock(), book.getImgPath());
    }

    @Override
    public int deleteBookById(Integer id) {
        String sql = "DELETE FROM t_book WHERE id = ?";
        return update(sql, id);
    }

    @Override
    public int updateBook(Book book) {
        String sql = "UPDATE t_book SET `name`=?,`author`=?,`price`=?,`sales`=?,`stock`=?,`img_path`=? WHERE id = ?";
        return update(sql, book.getName(), book.getAuthor(), book.getPrice(), book.getSales(), book.getStock(), book.getImgPath(), book.getId());
    }

    @Override
    public Book queryBookByID(Integer id) {
        String sql = "SELECT `id` , `name` , `author` , `price` , `sales` , `stock` , `img_path` imgPath FROM t_book WHERE id = ?";
        return queryForOne(Book.class, sql, id);
    }

    @Override
    public List<Book> queryBooks() {
        String sql = "SELECT `id` , `name` , `author` , `price` , `sales` , `stock` , `img_path` imgPath FROM t_book";
        return queryForList(Book.class, sql);
    }
}