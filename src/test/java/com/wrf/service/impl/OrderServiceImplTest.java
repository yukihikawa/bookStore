package com.wrf.service.impl;

import com.wrf.AppConfig;
import com.wrf.Bean.Cart;
import com.wrf.Bean.CartItem;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;

class OrderServiceImplTest {
    OrderServiceImpl orderService;

    {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.orderService  = context.getBean(OrderServiceImpl.class);

    }

    @Test
    void creatOrder() {
        Cart cart = new Cart();
        cart.addItem(new CartItem(1, "java从入门到精通", 1, new BigDecimal(1000),new BigDecimal(1000)));
        cart.addItem(new CartItem(1, "java从入门到精通", 1, new BigDecimal(1000),new BigDecimal(1000)));
        cart.addItem(new CartItem(2, "数据结构与算法", 1, new BigDecimal(100),new BigDecimal(100)));
        System.out.println( "订单号是：" + orderService.createOrder(cart, 1) );

    }
}