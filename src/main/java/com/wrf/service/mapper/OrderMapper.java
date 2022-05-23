package com.wrf.service.mapper;

import com.wrf.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @program: bookStore
 * @description: 订单模块
 * @author: Rifu Wu
 * @create: 2022-05-22 15:47
 **/
public interface OrderMapper {

    /**创建订单
     * @param order
     */
    @Insert("INSERT INTO t_order(order_id, create_time, price, status, user_id) VALUES(#{order.orderId}, #{order.createTime}, #{order.price}, #{order.status}, #{order.userId})")
    void saveOrder(@Param("order") Order order);

    /**查询所有订单
     * @return
     */
    @Select("SELECT order_id orderId, create_time createTime, price, status, user_id userId FROM t_order")
    List<Order> queryOrders();

    /**修改订单状态
     * @param orderId
     * @param status
     * @return
     */
    @Update("UPDATE t_order SET status = #{status} WHERE order_id = #{orderId}")
    int changeOrderStatus(@Param("orderId") String orderId, @Param("status") Integer status);

    /**查询特定用户订单
     * @param userId
     * @return
     */
    @Select("SELECT order_id orderId, create_time createTime, price, status, user_id userId FROM t_order WHERE user_id = #{userId}")
    List<Order> queryOrdersByUserId(@Param("userId") Integer userId);
}