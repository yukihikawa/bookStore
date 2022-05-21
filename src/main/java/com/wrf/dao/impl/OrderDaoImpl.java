package com.wrf.dao.impl;

import com.wrf.Bean.Order;
import com.wrf.dao.BaseDao;
import com.wrf.dao.OrderDao;
import org.springframework.stereotype.Component;

import java.util.List;

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

    @Override
    public List<Order> queryOrders() {
        String sql ="SELECT `order_id` orderId, `create_time` createTime, `price`, `status`, `user_id` userId FROM t_order";
        return queryForList(Order.class, sql);
    }

    @Override
    public int changeOrderStatus(String orderId, Integer status) {
        String sql = "UPDATE t_order SET status = ? WHERE order_id = ?";
        return update(sql, status, orderId);
    }

    @Override
    public List<Order> queryOrdersByUserId(Integer userId) {
        String sql ="SELECT `order_id` orderId, `create_time` createTime, `price`, `status`, `user_id` userId FROM t_order WHERE user_id = ?";
        return queryForList(Order.class, sql, userId);
    }


}