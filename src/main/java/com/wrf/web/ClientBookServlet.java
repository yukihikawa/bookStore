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

/**
 * @program: bookStore
 * @description: 客户端
 * @author: Rifu Wu
 * @create: 2022-05-17 13:03
 **/
@Slf4j
public class ClientBookServlet extends BaseServlet{
    BookServiceImpl bookService;


    @Override
    public void init(ServletConfig config) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.bookService  = context.getBean(BookServiceImpl.class);
    }

    public void pageByPrice(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求的参数pageNo和pageSize
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);
        //调用pageService.page()获取page
        Page<Book> page = bookService.page(pageNo, pageSize);
        page.setUrl("client/clientbookServlet?action=pageByPrice");

        req.setAttribute("page", page);
        req.getRequestDispatcher("/pages/client/index.jsp").forward(req, resp);
    }
}