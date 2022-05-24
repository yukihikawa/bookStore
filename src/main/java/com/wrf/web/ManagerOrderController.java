package com.wrf.web;

import com.wrf.entity.Order;
import com.wrf.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @program: bookStore
 * @description: 订单管理
 * @author: Rifu Wu
 * @create: 2022-05-24 18:10
 **/
@Slf4j
@Controller
@AllArgsConstructor
public class ManagerOrderController {
    OrderService orderService;

    /**
     * 查看所有订单（管理员）
     * @param req
     * @param resp
     */
    @GetMapping("/manager/showAllOrders")
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
    @GetMapping(value = "manager/sendOrder", params = {"orderId"})
    protected void sendOrder(HttpServletRequest req, HttpServletResponse resp, String orderId) throws IOException {
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