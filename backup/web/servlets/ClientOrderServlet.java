package com.wrf.web.servlets;

import com.wrf.AppConfig;
import com.wrf.entity.Cart;
import com.wrf.entity.Order;
import com.wrf.entity.User;
import com.wrf.service.OrderService;
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
 * @description: 用户订单管理
 * @author: Rifu Wu
 * @create: 2022-05-20 22:37
 **/
@Slf4j
public class ClientOrderServlet extends BaseServlet{
    OrderService orderService;

    @Override
    public void init(ServletConfig config) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.orderService  = context.getBean(OrderService.class);
    }

    /**
     * c创建订单
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
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

    /**
     * 查看订单（用户
     * @param req
     * @param resp
     */
    protected void showMyOrders(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = (User) req.getSession().getAttribute("user");
        if(user == null)
            resp.sendRedirect(req.getContextPath() + "/pages/user/login.jsp");
        else {
            log.info(user.getId() + "");
            List<Order> orders = orderService.showMyOrders(user.getId());
            log.info(orders.toString());
            req.setAttribute("myOrders", orders);
            req.getRequestDispatcher("/pages/order/order.jsp").forward(req, resp);
        }
    }

    /**
     * 订单签收（用户
     * @param req
     * @param resp
     */
    protected void receiveOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String orderId = req.getParameter("orderId");
        orderService.receiverOrder(orderId);
        resp.sendRedirect(req.getHeader("referer"));
    }

    protected void showOrderDetail(HttpServletRequest req, HttpServletResponse resp){}


}