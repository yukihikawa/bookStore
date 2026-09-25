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
     * 创建订单（事务内）：
     * 先整体校验库存，再用"条件扣库存"（stock >= count 才更新）落库，
     * 由数据库行锁保证并发下不超卖；任一步失败抛异常回滚整个事务。
     * @param cart 购物车对象
     * @param userId 用户id
     * @return 订单号
     */
    public String createOrder(Cart cart, Integer userId) {
        //生成唯一订单号（与原实现一致：时间戳+userId）
        String orderId = System.currentTimeMillis()+""+userId;
        //创建订单对象
        Order order = new Order(orderId, LocalDateTime.now(), cart.getTotalPrice(), 0, userId);
        orderMapper.saveOrder(order);

        for(Map.Entry<Integer, CartItem> entry : cart.getItems().entrySet()){
            CartItem cartItem = entry.getValue();
            Book book = bookMapper.queryBookById(cartItem.getId());
            if (book == null) {
                throw new IllegalStateException("结算失败：书籍[id=" + cartItem.getId() + "]不存在，订单已回滚");
            }
            if (book.getStock() < cartItem.getCount()) {
                throw new IllegalStateException("结算失败：《" + book.getName() + "》库存不足（剩余 " + book.getStock() + "），订单已回滚");
            }
            //价格以购物车快照为准（用户实际支付金额）
            OrderItem orderItem = new OrderItem(null, book.getName(), cartItem.getCount(), cartItem.getPrice(), cartItem.getTotalPrice(), orderId);
            orderItemMapper.saveOrderItem(orderItem);

            // 条件扣库存：并发下库存不足时返回 0，抛异常回滚（含前面已写的订单/明细）
            int rows = bookMapper.decreaseStock(book.getId(), cartItem.getCount());
            if (rows == 0) {
                throw new IllegalStateException("结算失败：《" + book.getName() + "》库存不足，订单已回滚");
            }
            bookMapper.increaseSales(book.getId(), cartItem.getCount());
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