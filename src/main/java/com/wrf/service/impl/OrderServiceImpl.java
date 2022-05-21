package com.wrf.service.impl;

import com.wrf.Bean.*;
import com.wrf.dao.BookDao;
import com.wrf.dao.OrderDao;
import com.wrf.dao.OrderItemDao;
import com.wrf.dao.impl.BookDaoImpl;
import com.wrf.dao.impl.OrderDaoImpl;
import com.wrf.dao.impl.OrderItemDaoImpl;
import com.wrf.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
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
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final BookDao bookDao;

    public OrderServiceImpl(OrderDaoImpl orderDao, OrderItemDaoImpl orderItemDao, BookDaoImpl bookDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.bookDao = bookDao;
    }

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
        orderDao.saveOrder(order);

        for(Map.Entry<Integer, CartItem> entry : cart.getItems().entrySet()){
            CartItem cartItem = entry.getValue();
            OrderItem orderItem = new OrderItem(null, cartItem.getName(), cartItem.getCount(), cartItem.getPrice(), cartItem.getTotalPrice(), orderId);
            orderItemDao.saveOrderItem(orderItem);
            Book book = bookDao.queryBookByID(cartItem.getId());
            book.setSales(book.getSales() + cartItem.getCount());
            book.setStock(book.getStock() - cartItem.getCount());
            bookDao.updateBook(book);

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
        return orderDao.queryOrders();
    }

    /**标记订单发货
     * @param orderId
     * @return
     */
    @Override
    public int sendOrder(String orderId) {
        return orderDao.changeOrderStatus(orderId, 1);
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @Override
    public List<OrderItem> showOrderDetail(String orderId) {
        return orderItemDao.queryOrderItemsByOrderId(orderId);
    }

    @Override
    public List<Order> showMyOrders(Integer userId) {
        log.info("Show My Orders, user: " + userId);
        return orderDao.queryOrdersByUserId(userId);
    }

    @Override
    public int receiverOrder(String orderId) {
        return orderDao.changeOrderStatus(orderId, 2);
    }
}