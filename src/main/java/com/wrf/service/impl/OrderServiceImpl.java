package com.wrf.service.impl;

import com.wrf.Bean.*;
import com.wrf.dao.BookDao;
import com.wrf.dao.OrderDao;
import com.wrf.dao.OrderItemDao;
import com.wrf.dao.impl.BookDaoImpl;
import com.wrf.dao.impl.OrderDaoImpl;
import com.wrf.dao.impl.OrderItemDaoImpl;
import com.wrf.service.OrderService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @program: bookStore
 * @description:
 * @author: Rifu Wu
 * @create: 2022-05-20 14:40
 **/
@Component
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final BookDao bookDao;

    public OrderServiceImpl(OrderDaoImpl orderDao, OrderItemDaoImpl orderItemDao, BookDaoImpl bookDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.bookDao = bookDao;
    }

    @Override
    public String createOrder(Cart cart, Integer userId) {
        //生成唯一订单号
        String orderId = System.currentTimeMillis()+""+userId;
        //创建订单对象
        Order order = new Order(orderId, new Date(), cart.getTotalPrice(), 0, userId);
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
}