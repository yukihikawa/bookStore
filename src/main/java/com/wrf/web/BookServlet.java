package com.wrf.web;

import com.wrf.AppConfig;
import com.wrf.Bean.Book;
import com.wrf.Bean.Page;
import com.wrf.service.impl.BookServiceImpl;
import com.wrf.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @program: bookStore
 * @description: 图书管理
 * @author: Rifu Wu
 * @create: 2022-05-15 20:59
 **/
@Slf4j
public class BookServlet extends BaseServlet {
    BookServiceImpl bookService;


    @Override
    public void init(ServletConfig config) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.bookService  = context.getBean(BookServiceImpl.class);
    }

    //响应图书管理请求，列出所有图书
    public void listAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Book> books = bookService.getBookList();
        req.setAttribute("books", books);
        log.info("list request");
        req.getRequestDispatcher("/pages/manager/book_manager.jsp").forward(req, resp);
    }
    //按id删除图书
    public void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("delete req");
        String id = req.getParameter("id");
        bookService.deleteBook(id);
        /*listAll(req,resp);*/
        resp.sendRedirect(req.getContextPath() + "/manager/bookServlet?action=listAll");
    }

    public void addBook(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        log.info("servlet get add req");
        log.info(req.getParameter("name"));
        Book book = WebUtils.copyParamToBean(req.getParameterMap(), new Book());
        log.info("book" + book.getName());
        bookService.addBook(book);
        resp.sendRedirect(req.getContextPath() + "/manager/bookServlet?action=listAll");
    }

    /**
     * 添加书籍
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getBook(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("update book");
        String id = req.getParameter("id");
        Book book = bookService.getById(id);
        req.setAttribute("book", book);
        req.getRequestDispatcher("/pages/manager/book_edit.jsp").forward(req, resp);
    }

    /**
     * @param req
     * @param resp
     * @throws IOException
     */
    //更新图书信息
    public void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Book book = WebUtils.copyParamToBean(req.getParameterMap(), new Book());
        bookService.updateBook(book);
        resp.sendRedirect(req.getContextPath() + "/manager/bookServlet?action=listAll");
    }


    /**
     * 处理分页功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void page(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求的参数pageNo和pageSize
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);
        //调用pageService.page()获取page
        Page<Book> page = bookService.page(pageNo, pageSize);

        req.setAttribute("page", page);
        req.getRequestDispatcher("/pages/manager/book_manager.jsp").forward(req,resp);
    }

}