package com.wrf.dao.impl;

import com.wrf.AppConfig;
import com.wrf.dao.OrderDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
class OrderDaoImplTest {
    OrderDao orderDao;
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.orderDao  = context.getBean(OrderDao.class);
    }
    @Test
    void saveOrder() {
    }

    @Test
    void queryOrders() {
        log.info(orderDao.queryOrders().toString());
    }

    @Test
    void changeOrderStatus() {
        log.info(orderDao.changeOrderStatus("165303961632080", 0)+"");
    }

    @Test
    void queryOrdersByUserId() {
        log.info(orderDao.queryOrdersByUserId(80).toString());
    }
}