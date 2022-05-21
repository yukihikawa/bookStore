package com.wrf.web;

import com.wrf.AppConfig;
import com.wrf.Bean.Order;
import com.wrf.service.impl.OrderServiceImpl;
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
 * @description:
 * @author: Rifu Wu
 * @create: 2022-05-20 16:18
 **/
@Slf4j
public class ManagerOrderServlet extends BaseServlet{

    private OrderServiceImpl orderService;

    @Override
    public void init(ServletConfig config) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.orderService  = context.getBean(OrderServiceImpl.class);
    }


    /**
     * 查看所有订单（管理员）
     * @param req
     * @param resp
     */
    protected void showAllOrders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Order> orders = orderService.showAllOrders();
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/pages/manager/order_manager.jsp").forward(req, resp);
    }

    /**
     * 发货（管理员）
     * @param req
     * @param resp
     */
    protected void sendOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String orderId = req.getParameter("orderId");
        orderService.sendOrder(orderId);
        resp.sendRedirect(req.getHeader("referer"));
    }

    /**
     * 查看订单详情（管理员）
     * @param req
     * @param resp
     */
    protected void showOrderDetail(HttpServletRequest req, HttpServletResponse resp){}


}