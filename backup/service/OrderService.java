package com.wrf.service;

import com.wrf.Bean.Cart;
import com.wrf.Bean.Order;
import com.wrf.Bean.OrderItem;

import java.util.List;

/**
 * @program: bookStore
 * @description: 订单模块service
 * @author: Rifu Wu
 * @create: 2022-05-20 00:29
 **/
public interface OrderService {
    public String createOrder(Cart cart, Integer userId);

    public List<Order> showAllOrders();

    public int sendOrder(String orderId);

    public List<OrderItem> showOrderDetail(String orderId);

    public List<Order> showMyOrders(Integer userId);

    public int receiverOrder(String orderId);

}
