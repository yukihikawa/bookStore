package com.wrf.dao.impl;

import com.wrf.AppConfig;
import com.wrf.dao.OrderItemDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
class OrderItemDaoImplTest {
    OrderItemDao orderItemDao;
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.orderItemDao  = context.getBean(OrderItemDao.class);
    }

    @Test
    void saveOrderItem() {
    }

    @Test
    void queryOrderItemsByOrderId() {
        log.info(orderItemDao.queryOrderItemsByOrderId("165303961632080").toString());
    }
}