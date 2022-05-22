package com.wrf.service.impl;

import com.wrf.AppConfig;
import com.wrf.Bean.Cart;
import com.wrf.Bean.CartItem;
import com.wrf.Bean.Order;
import com.wrf.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
class OrderServiceImplTest {
    OrderService orderService;

    {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.orderService  = context.getBean(OrderService.class);

    }

    @Test
    void creatOrder() {
        Cart cart = new Cart();
        cart.addItem(new CartItem(1, "java从入门到精通", 1, new BigDecimal(1000),new BigDecimal(1000)));
        cart.addItem(new CartItem(1, "java从入门到精通", 1, new BigDecimal(1000),new BigDecimal(1000)));
        cart.addItem(new CartItem(2, "数据结构与算法", 1, new BigDecimal(100),new BigDecimal(100)));
        System.out.println( "订单号是：" + orderService.createOrder(cart, 1) );

    }

    @Test
    void showAllOrders() {
    }

    @Test
    void sendOrder() {
    }

    @Test
    void showOrderDetail() {
    }

    @Test
    void showMyOrders() {
        List<Order> orders = orderService.showMyOrders(80);
        log.info(orders.toString());
    }

    @Test
    void receiverOrder() {
    }
}