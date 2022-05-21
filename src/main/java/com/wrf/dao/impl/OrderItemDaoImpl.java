package com.wrf.dao.impl;

import com.wrf.Bean.OrderItem;
import com.wrf.dao.BaseDao;
import com.wrf.dao.OrderItemDao;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: bookStore
 * @description:
 * @author: Rifu Wu
 * @create: 2022-05-19 23:58
 **/
@Component
public class OrderItemDaoImpl extends BaseDao implements OrderItemDao {
    @Override
    public int saveOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO t_order_item(`name`,`count`,`price`,`total_price`,`order_id`)VALUES(?,?,?,?,?)";
        return update(sql, orderItem.getName(), orderItem.getCount(), orderItem.getPrice(), orderItem.getTotalPrice(), orderItem.getOrderId());
    }

    @Override
    public List<OrderItem> queryOrderItemsByOrderId(String orderId) {
        String sql = "SELECT `id`, `name`, `count`, `price`, `total_price` totalPrice, `order_id` orderId FROM t_order_item WHERE order_id = ?";
        return queryForList(OrderItem.class, sql, orderId);
    }
}