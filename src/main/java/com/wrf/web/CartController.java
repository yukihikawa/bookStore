package com.wrf.web;

import com.wrf.AppConfig;
import com.wrf.entity.Book;
import com.wrf.entity.Cart;
import com.wrf.entity.CartItem;
import com.wrf.service.BookService;
import com.wrf.service.utils.WebUtils;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: bookStore
 * @description: 购物车模块
 * @author: Rifu Wu
 * @create: 2022-05-24 15:48
 **/
@Slf4j
@Controller
@AllArgsConstructor
public class CartController {
    BookService bookService;


    /**
     * 加入购物车
     * @param req
     * @param resp
     */
    @GetMapping(value = "/addItem", params = {"id"})
    public void addItem(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {

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
    @GetMapping(value = "/deleteItem", params = {"id"})
    public void deleteItem(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
        log.info("map to deleteItem");
        int idi = WebUtils.parseInt(id, 0);
        Cart cart = (Cart) req.getSession().getAttribute("cart");

        if(cart != null){
            cart.deleteItem(idi);
            log.info("delete id: " + idi);
            resp.sendRedirect(req.getHeader("Referer"));
        }

    }

    /**
     * 清空购物车
     * @param req
     * @param resp
     */
    @GetMapping("/clear")
    public void clear(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cart cart = (Cart) req.getSession().getAttribute("cart");

        if(cart != null){
            cart.clear();
            resp.sendRedirect(req.getHeader("Referer"));
        }

    }

    @GetMapping(value = "/updateCount", params = {"counts", "ids"})
    public void updateCount(HttpServletRequest req, HttpServletResponse resp, String counts, String ids) throws IOException {
        int id = WebUtils.parseInt(ids, 0);
        int count = WebUtils.parseInt(counts ,1);
        /*log.info("id: " + id + " new count: " + count );*/
        Cart cart = (Cart) req.getSession().getAttribute("cart");

        if(cart != null){
            cart.updateCount(id, count);
            resp.sendRedirect(req.getHeader("Referer"));

        }
        log.info("Cart update...");

    }
}