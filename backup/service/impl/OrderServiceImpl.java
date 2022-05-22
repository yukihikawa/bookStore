package com.wrf.service.impl;

import com.wrf.Bean.*;
import com.wrf.dao.OrderItemDao;
import com.wrf.mapper.BookMapper;
import com.wrf.mapper.OrderItemMapper;
import com.wrf.mapper.OrderMapper;
import com.wrf.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @program: bookStore
 * @description:
 * @author: Rifu Wu
 * @create: 2022-05-20 14:40
 **/
@Component
@Slf4j
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    OrderMapper orderMapper;
    OrderItemMapper orderItemMapper;
    BookMapper bookMapper;



    /**
     * 创建订单
     * @param cart 购物车对象
     * @param userId 用户id
     * @return
     */
    @Override
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
    @Override
    public List<Order> showAllOrders() {
        return orderMapper.queryOrders();
    }

    /**标记订单发货
     * @param orderId
     * @return
     */
    @Override
    public int sendOrder(String orderId) {
        return orderMapper.changeOrderStatus(orderId, 1);
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @Override
    public List<OrderItem> showOrderDetail(String orderId) {
        return orderItemMapper.queryOrderItemsByOrderId(orderId);
    }

    @Override
    public List<Order> showMyOrders(Integer userId) {
        log.info("Show My Orders, user: " + userId);
        return orderMapper.queryOrdersByUserId(userId);
    }

    @Override
    public int receiverOrder(String orderId) {
        return orderMapper.changeOrderStatus(orderId, 2);
    }
}