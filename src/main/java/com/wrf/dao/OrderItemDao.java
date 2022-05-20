package com.wrf.dao;

import com.wrf.Bean.OrderItem;

/**
 * @program: bookStore
 * @description: 订单条目项
 * @author: Rifu Wu
 * @create: 2022-05-19 23:37
 **/
public interface OrderItemDao {
    public int saveOrderItem(OrderItem orderItem);
}
