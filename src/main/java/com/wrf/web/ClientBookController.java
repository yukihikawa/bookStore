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
 * @description:
 * @author: Rifu Wu
 * @create: 2022-05-23 22:08
 **/
@Controller
@Slf4j
@AllArgsConstructor
public class ClientBookController {
    BookService bookService;

    @GetMapping("/")
    public void ind(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/index.jsp").forward(req, resp);
    }

    @GetMapping(value = "/client/pageByPrice/{pno}")
    public void page(HttpServletRequest req, HttpServletResponse resp, @PathVariable String pno) throws ServletException, IOException {
        //获取请求的参数pageNo和pageSize
        int pageNo = WebUtils.parseInt(pno, 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);

        //调用pageService.page()获取page
        log.info("@CBServlet : " + pageNo +" " + pageSize);
        Page<Book> page = bookService.page(pageNo, pageSize);
        String url = "client/pageByPrice";
        page.setUrl(url);

        req.setAttribute("page", page);
        req.getRequestDispatcher("/pages/client/index.jsp").forward(req, resp);
    }

    @GetMapping(value ={ "/client/pageByPrice/{min}/{max}/{pno}"})
    public void pageByPrice(HttpServletRequest req, HttpServletResponse resp, @PathVariable String pno, @PathVariable String min, @PathVariable String max) throws ServletException, IOException {
        //获取请求的参数pageNo和pageSize
        int pageNo = WebUtils.parseInt(pno, 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);

        int minc = WebUtils.parseInt(min, 0);
        int maxc = WebUtils.parseInt(max, Integer.MAX_VALUE);
        //调用pageService.page()获取page
        log.info("@CBServlet : " + pageNo +" " + pageSize + " " + minc + " " + maxc);
        Page<Book> page = bookService.pageByPrice(pageNo, pageSize, minc, maxc);
        log.info("@CBServlet : ");
        log.info(page.getItems().toString());
        String url = "client/pageByPrice";
        if (min != null && !"".equals(min)) {
            url += "/" + min;
        }
        if (max != null && !"".equals(max)) {
            url += "/" + max;
        }
        page.setUrl(url);

        req.setAttribute("page", page);
        req.getRequestDispatcher("/pages/client/index.jsp").forward(req, resp);
    }

    @GetMapping(value = "/client/pageByPrice", params = {"pno", "min", "max"})
    public void pageByPriceRe(HttpServletRequest req, HttpServletResponse resp, String pno, String min, String max) throws ServletException, IOException {
        //获取请求的参数pageNo和pageSize
        this.pageByPrice(req, resp, pno, min, max);
    }
}