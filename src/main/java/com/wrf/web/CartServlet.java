package com.wrf.web;

import com.wrf.AppConfig;
import com.wrf.Bean.Book;
import com.wrf.Bean.Cart;
import com.wrf.Bean.CartItem;
import com.wrf.service.BookService;
import com.wrf.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: bookStore
 * @description: 购物车
 * @author: Rifu Wu
 * @create: 2022-05-19 12:46
 **/
@Slf4j
public class CartServlet extends BaseServlet{
    BookService bookService;

    @Override
    public void init(ServletConfig config) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.bookService  = context.getBean(BookService.class);
    }

    /**
     * 加入购物车
     * @param req
     * @param resp
     */
    public void addItem(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        Book book = bookService.getById(id);
        //创建商品项
        CartItem cartItem  = new CartItem(book.getId(), book.getName(), 1, book.getPrice(), book.getPrice());
        //添加商品项
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        if(cart == null){
            cart = new Cart();
            req.getSession().setAttribute("cart", cart);
        }
        cart.addItem(cartItem);
        log.info(cart.toString());
        log.info("请求头Referer ：" + req.getHeader("Referer"));
        req.getSession().setAttribute("lastname", cartItem.getName());

        resp.sendRedirect(req.getHeader("Referer"));
    }

    /**
     * 删除商品项
     * @param req
     * @param resp
     */
    public void deleteItem(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = WebUtils.parseInt(req.getParameter("id"), 0);
        Cart cart = (Cart) req.getSession().getAttribute("cart");

        if(cart != null){
            cart.deleteItem(id);
            log.info("delete id: " + id);
            resp.sendRedirect(req.getHeader("Referer"));
        }

    }

    /**
     * 清空购物车
     * @param req
     * @param resp
     */
    public void clear(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cart cart = (Cart) req.getSession().getAttribute("cart");

        if(cart != null){
            cart.clear();
            resp.sendRedirect(req.getHeader("Referer"));
        }

    }

    public void updateCount(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = WebUtils.parseInt(req.getParameter("id"), 0);
        int count = WebUtils.parseInt(req.getParameter("count") ,1);
        /*log.info("id: " + id + " new count: " + count );*/
        Cart cart = (Cart) req.getSession().getAttribute("cart");

        if(cart != null){
            cart.updateCount(id, count);
            resp.sendRedirect(req.getHeader("Referer"));

        }
        log.info("Cart update...");

    }
}