package com.wrf.service;

import com.wrf.entity.*;
import com.wrf.service.mapper.BookMapper;
import com.wrf.service.mapper.OrderItemMapper;
import com.wrf.service.mapper.OrderMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @program: bookStore
 * @description: 订单服务
 * @author: Rifu Wu
 * @create: 2022-05-22 16:47
 **/
@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class OrderService {
    OrderMapper orderMapper;
    OrderItemMapper orderItemMapper;
    BookMapper bookMapper;

    /**
     * 创建订单
     * @param cart 购物车对象
     * @param userId 用户id
     * @return
     */
    public String createOrder(Cart cart, Integer userId) {
        //生成唯一订单号
        String orderId = System.currentTimeMillis()+""+userId;
        //创建订单对象
        Order order = new Order(orderId, LocalDateTime.now(), cart.getTotalPrice(), 0, userId);
        orderMapper.saveOrder(order);

        for(Map.Entry<Integer, CartItem> entry : cart.getItems().entrySet()){
            CartItem cartItem = entry.getValue();
            OrderItem orderItem = new OrderItem(null, cartItem.getName(), cartItem.getCount(), cartItem.getPrice(), cartItem.getTotalPrice(), orderId);
            orderItemMapper.saveOrderItem(orderItem);
            Book book = bookMapper.queryBookById(cartItem.getId());
            book.setSales(book.getSales() + cartItem.getCount());
            book.setStock(book.getStock() - cartItem.getCount());
            bookMapper.updateBook(book);

        }

        cart.clear();
        return orderId;
    }

    /**
     * 获取所有订单的列表
     * @return
     */
    public List<Order> showAllOrders() {
        return orderMapper.queryOrders();
    }

    /**标记订单发货
     * @param orderId
     * @return
     */
    public int sendOrder(String orderId) {
        return orderMapper.changeOrderStatus(orderId, 1);
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    public List<OrderItem> showOrderDetail(String orderId) {
        return orderItemMapper.queryOrderItemsByOrderId(orderId);
    }

    public List<Order> showMyOrders(Integer userId) {
        log.info("Show My Orders, user: " + userId);
        return orderMapper.queryOrdersByUserId(userId);
    }

    public int receiverOrder(String orderId) {
        return orderMapper.changeOrderStatus(orderId, 2);
    }
}