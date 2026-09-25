package com.wrf.web;

import com.wrf.entity.Book;
import com.wrf.entity.Cart;
import com.wrf.entity.CartItem;
import com.wrf.service.BookService;
import com.wrf.service.utils.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
     * 加入购物车（带库存校验：购物车中已有数量 + 1 不得超过当前库存）
     * @param req
     * @param resp
     */
    @GetMapping(value = "/addItem", params = {"id"})
    public void addItem(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {

        Book book = bookService.getById(id);
        //添加商品项
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        if(cart == null){
            cart = new Cart();
            req.getSession().setAttribute("cart", cart);
        }
        if(cart.getCount(book.getId()) + 1 > book.getStock()){
            //库存不足：JSP 页面无统一错误通道，仅打日志，回跳来源页
            log.warn("stock not enough for book [{}], in cart: [{}], stock: [{}]",
                    book.getName(), cart.getCount(book.getId()), book.getStock());
        }
        else {
            //创建商品项
            CartItem cartItem  = new CartItem(book.getId(), book.getName(), 1, book.getPrice(), book.getPrice());
            cart.addItem(cartItem);
            log.info(cart.toString());
            req.getSession().setAttribute("lastname", cartItem.getName());
        }
        log.info("请求头Referer ：" + req.getHeader("Referer"));

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

    /**
     * 修改数量（带库存校验；结算时 OrderService 还有一次数据库层校验兜底）
     */
    @GetMapping(value = "/updateCount", params = {"counts", "ids"})
    public void updateCount(HttpServletRequest req, HttpServletResponse resp, String counts, String ids) throws IOException {
        int id = WebUtils.parseInt(ids, 0);
        int count = WebUtils.parseInt(counts ,1);
        /*log.info("id: " + id + " new count: " + count );*/
        Cart cart = (Cart) req.getSession().getAttribute("cart");

        if(cart != null){
            Book book = bookService.getById(String.valueOf(id));
            if(count > book.getStock()){
                log.warn("stock not enough for book [{}], want: [{}], stock: [{}]",
                        book.getName(), count, book.getStock());
            }
            else {
                cart.updateCount(id, count);
            }
            resp.sendRedirect(req.getHeader("Referer"));

        }
        log.info("Cart update...");

    }
}
