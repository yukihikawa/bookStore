package com.wrf.service;

import com.wrf.Bean.Cart;

/**
 * @program: bookStore
 * @description: 订单模块service
 * @author: Rifu Wu
 * @create: 2022-05-20 00:29
 **/
public interface OrderService {
    public String createOrder(Cart cart, Integer userId);
}
