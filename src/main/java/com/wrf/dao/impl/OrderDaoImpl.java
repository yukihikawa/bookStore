package com.wrf.dao.impl;

import com.wrf.Bean.Order;
import com.wrf.dao.BaseDao;
import com.wrf.dao.OrderDao;
import org.springframework.stereotype.Component;

/**
 * @program: bookStore
 * @description: OrderDao实现类
 * @author: Rifu Wu
 * @create: 2022-05-19 23:14
 **/
@Component
public class OrderDaoImpl  extends BaseDao implements OrderDao{
    @Override
    public int saveOrder(Order order) {
        String sql = "INSERT INTO t_order(`order_id`,`create_time`,`price`,`status`,`user_id`)VALUES(?,?,?,?,?)";

        return update(sql, order.getOrderId(), order.getCreateTime(), order.getPrice(), order.getStatus(), order.getUserId())
;    }
}