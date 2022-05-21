package com.wrf.dao;

import com.wrf.Bean.Order;

import java.util.List;

/**
 * @program: bookStore
 * @description: 订单
 * @author: Rifu Wu
 * @create: 2022-05-19 23:02
 **/
public interface OrderDao {
    public int saveOrder(Order order);

    public List<Order> queryOrders();

    public int changeOrderStatus(String orderId, Integer status);

    public List<Order> queryOrdersByUserId(Integer userId);
}
