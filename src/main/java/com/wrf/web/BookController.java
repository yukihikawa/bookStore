package com.wrf.web;

import com.wrf.entity.Book;
import com.wrf.entity.Page;
import com.wrf.service.BookService;
import com.wrf.service.utils.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: bookStore
 * @description: 书籍管理模块
 * @author: Rifu Wu
 * @create: 2022-05-24 00:01
 **/
@Controller
@Slf4j
@AllArgsConstructor
public class BookController {
    BookService bookService;
    //按id删除图书
    @GetMapping("/manager/delete/{id}/{pageNo}")
    public void delete(HttpServletRequest req, HttpServletResponse resp, @PathVariable("id") String id, @PathVariable("pageNo") String pageNo) throws ServletException, IOException {
        log.info("delete req");

        bookService.deleteBook(id);
        /*listAll(req,resp);*/
        /*resp.sendRedirect(req.getContextPath() + "/manager/bookServlet?action=listAll");*/
        resp.sendRedirect(req.getContextPath() + "/manager/page/" + pageNo);
    }

    /**添加书籍
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    @GetMapping("/manager/addBook")
    public void addBook(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        log.info("servlet get add req");

        Book book = WebUtils.copyParamToBean(req.getParameterMap(), new Book());
        log.info("new book: " + book.getName());
        bookService.addBook(book);
        resp.sendRedirect(req.getContextPath() + "/manager/page/1");
    }

    /**
     * 获取书籍
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping("/manager/getBook/{id}/{pno}")
    public void getBook(HttpServletRequest req, HttpServletResponse resp, @PathVariable("id") String id, @PathVariable("pno") String pno) throws ServletException, IOException {
        log.info("update book");
        Book book = bookService.getById(id);
        req.setAttribute("book", book);
        req.setAttribute("pageNo", pno);
        req.getRequestDispatcher("/pages/manager/book_edit.jsp").forward(req, resp);
    }

    /**
     * @param req
     * @param resp
     * @throws IOException
     */
    //更新图书信息
    @GetMapping("/manager/update")
    public void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Book book = WebUtils.copyParamToBean(req.getParameterMap(), new Book());
        bookService.updateBook(book);
        resp.sendRedirect(req.getContextPath() + "/manager/page/" + req.getParameter("pageNo"));
    }


    /**
     * 处理分页功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping("/manager/page/{pno}")
    protected void page(HttpServletRequest req, HttpServletResponse resp, @PathVariable("pno") String pno) throws ServletException, IOException {
        //获取请求的参数pageNo和pageSize
        int pageNo = WebUtils.parseInt(pno, 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);
        /*log.info("pageno : " + pageNo);*/
        //调用pageService.page()获取page
        Page<Book> page = bookService.page(pageNo, pageSize);
        //设置URL
        page.setUrl("manager/page");
        req.setAttribute("page", page);
        req.getRequestDispatcher("/pages/manager/book_manager.jsp").forward(req,resp);
    }

}