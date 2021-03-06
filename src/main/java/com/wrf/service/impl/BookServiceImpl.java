package com.wrf.service.impl;

import com.wrf.Bean.Page;
import com.wrf.dao.BookDao;
import com.wrf.dao.impl.BookDaoImpl;
import com.wrf.Bean.Book;
import com.wrf.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: bookStore
 * @description: BookService实现类
 * @author: Rifu Wu
 * @create: 2022-05-15 17:22
 **/
@Slf4j
@Component
public class BookServiceImpl implements BookService {

    final
    BookDao bookDao;

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

    //获取页面
    @Override
    public Page<Book> page(int pageNo, int pageSize) {
        Page<Book> page = new Page<>();
        //设置页面大小（每页数量
        page.setPageSize(pageSize);
        //总记录数
        Integer pageTotalCount = bookDao.queryForPageTotalCount();
        //总条目数
        page.setPageTotalCount(pageTotalCount);
        //总页码
        Integer pageTotal = pageTotalCount / pageSize;
        if(pageTotalCount % pageSize > 0)
            pageTotal += 1;
        page.setPageTotal(pageTotal);

        //设置页码
        page.setPageNo(pageNo);

        //当前页起始索引
        int begin = (page.getPageNo() - 1) * pageSize;
        //当前页面条目
        List<Book> items = bookDao.queryForPageElement(begin, pageSize);
        page.setItems(items);

        return page;
    }

    @Override
    public Page<Book> pageByPrice(int pageNo, int pageSize, int min, int max) {
        Page<Book> page = new Page<>();
        page.setPageSize(pageSize);
        Integer pageTotalCount = bookDao.queryForPageTotalCount(min, max);
        page.setPageTotalCount(pageTotalCount);

        Integer pageTotal = pageTotalCount / pageSize;
        if(pageTotalCount % pageSize > 0)
            pageTotal += 1;
        page.setPageTotal(pageTotal);
        //设置页码
        page.setPageNo(pageNo);
        int begin = ( page.getPageNo() - 1 ) * page.getPageSize();
        if(begin < 0)
            begin= 0;
        int size = page.getPageSize();
        /*log.info("begin : " + begin);
        log.info("size : " + size);
*/
        List<Book> items = bookDao.queryForPageElement(begin, size, min, max);

        page.setItems(items);
        return page;
    }
}