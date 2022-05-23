package com.wrf.service;

import com.wrf.entity.Book;
import com.wrf.entity.Page;
import com.wrf.service.mapper.BookMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: bookStore
 * @description: 书籍管理模块服务
 * @author: Rifu Wu
 * @create: 2022-05-22 14:46
 **/
@Component
@Transactional
@Slf4j
@AllArgsConstructor
public class BookService {

    BookMapper bookMapper;


    public void addBook(Book book) {
        bookMapper.addBook(book);
    }


    public void deleteBook(String bookId) {
        bookMapper.deleteBookById(Integer.valueOf(bookId));
    }


    public void updateBook(Book book) {
        bookMapper.updateBook(book);
    }


    public Book getById(String bookId) {
        return bookMapper.queryBookById(Integer.valueOf(bookId));
    }


    public List<Book> getBookList() {
        return bookMapper.queryBooks();
    }

    //获取页面

    public Page<Book> page(int pageNo, int pageSize) {
        Page<Book> page = new Page<>();
        //设置页面大小（每页数量
        page.setPageSize(pageSize);
        //总记录数
        Integer pageTotalCount = bookMapper.queryForPageTotalCount();
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
        List<Book> items = bookMapper.queryForPageElement(begin, pageSize);
        page.setItems(items);

        return page;
    }


    public Page<Book> pageByPrice(int pageNo, int pageSize, int min, int max) {
        Page<Book> page = new Page<>();
        page.setPageSize(pageSize);
        Integer pageTotalCount = bookMapper.queryForPageTotalCountScreen(min, max);
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
        List<Book> items = bookMapper.queryForPageElementScreen(begin, size, min, max);

        page.setItems(items);
        return page;
    }
}