package com.wrf.mapper;

import com.wrf.Bean.OrderItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @program: bookStore
 * @description: 订单项模块
 * @author: Rifu Wu
 * @create: 2022-05-22 16:30
 **/
public interface OrderItemMapper {

    /**
     * @param orderItem
     * @return
     */
    @Insert("INSERT INTO t_order_item(name, count, price, total_price, order_id)VALUES(#{orderItem.name}, #{orderItem.count}, #{orderItem.price}, #{orderItem.totalPrice}, #{orderItem.orderId})")
    int saveOrderItem(@Param("orderItem") OrderItem orderItem);

    /**查询订单中的项目
     * @param orderId
     * @return
     */
    @Select("SELECT id, name, count, price, total_price totalPrice, order_id orderId FROM t_order_item WHERE order_id = #{orderId}")
    List<OrderItem> queryOrderItemsByOrderId(@Param("orderId") String orderId);
}
