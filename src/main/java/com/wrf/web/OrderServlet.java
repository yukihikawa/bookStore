package com.wrf.web;

import com.wrf.AppConfig;
import com.wrf.Bean.Cart;
import com.wrf.Bean.User;
import com.wrf.service.impl.OrderServiceImpl;
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
 * @description:
 * @author: Rifu Wu
 * @create: 2022-05-20 16:18
 **/
@Slf4j
public class OrderServlet extends BaseServlet{

    private OrderServiceImpl orderService;

    @Override
    public void init(ServletConfig config) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.orderService  = context.getBean(OrderServiceImpl.class);
    }

    protected void createOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        User loginUser = (User) req.getSession().getAttribute("user");
        log.info("loginUser: " + loginUser);
        if(loginUser == null){
            req.getRequestDispatcher("/pages/user/login.jsp").forward(req,resp);
            return;
        }

        //创建订单
        Integer userId = loginUser.getId();
        log.info("userid: " + userId);
        String orderId = orderService.createOrder(cart, userId);

        req.getSession().setAttribute("orderId", orderId);

        resp.sendRedirect(req.getContextPath() + "/pages/cart/checkout.jsp");
    }


}