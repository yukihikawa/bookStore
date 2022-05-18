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
        String minStr = req.getParameter("min");
        String maxStr = req.getParameter("max");
        int min = WebUtils.parseInt(minStr, 0);
        int max = WebUtils.parseInt(maxStr, Integer.MAX_VALUE);
        //调用pageService.page()获取page
        log.info("@CBServlet : " + pageNo +" " + pageSize + " " + min + " " + max);
        Page<Book> page = bookService.pageByPrice(pageNo, pageSize, min, max);
        /*log.info("@CBServlet : ");
        log.info(page.getItems().toString());*/
        String url = "client/clientbookServlet?action=pageByPrice";
        if (minStr != null && !"".equals(minStr)) {
            url += "&min=" + minStr;
        }
        if (maxStr != null && !"".equals(maxStr)) {
            url += "&max=" + maxStr;
        }
        page.setUrl(url);

        req.setAttribute("page", page);
        req.getRequestDispatcher("/pages/client/index.jsp").forward(req, resp);
    }
}