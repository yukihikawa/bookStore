package com.wrf.web.api;

import com.wrf.entity.Order;
import com.wrf.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: bookStore
 * @description: SPA 订单相关接口
 *   GET /api/orders/my                     我的订单
 *   GET /api/orders/my/{orderId}/items     我的订单详情
 *   POST /api/orders/{orderId}/receive     确认收货
 *   GET /api/orders/all                    后台：全部订单（需管理员）
 *   POST /api/orders/{orderId}/send        后台：发货（需管理员）
 *   GET /api/orders/{orderId}/items        后台：订单明细（需管理员）
 * @author: Rifu Wu
 **/
@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final AuthSupport authSupport;

    @GetMapping("/my")
    public List<Order> myOrders(HttpServletRequest request) {
        var user = requireUser(request);
        return orderService.showMyOrders(user.getId());
    }

    /** 当前用户的订单明细 */
    @GetMapping("/my/{orderId}/items")
    public List<com.wrf.entity.OrderItem> myOrderItems(HttpServletRequest request,
                                                       @PathVariable("orderId") String orderId) {
        var user = requireUser(request);
        boolean owned = orderService.showMyOrders(user.getId()).stream()
                .anyMatch(o -> o.getOrderId().equals(orderId));
        if (!owned) {
            throw new ApiException(ApiCodes.NOT_FOUND, "订单不存在");
        }
        return orderService.showOrderDetail(orderId);
    }

    /** 用户确认收货 */
    @PostMapping("/my/{orderId}/receive")
    public Map<String, Object> receive(HttpServletRequest request,
                                       @PathVariable("orderId") String orderId) {
        var user = requireUser(request);
        boolean owned = orderService.showMyOrders(user.getId()).stream()
                .anyMatch(o -> o.getOrderId().equals(orderId));
        if (!owned) {
            throw new ApiException(ApiCodes.NOT_FOUND, "订单不存在");
        }
        orderService.receiverOrder(orderId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "已签收");
        return result;
    }

    // ------- 管理端 -------

    @GetMapping("/all")
    public List<Order> allOrders(HttpServletRequest request) {
        requireAdmin(request);
        return orderService.showAllOrders();
    }

    @PostMapping("/{orderId}/send")
    public Map<String, Object> send(HttpServletRequest request,
                                    @PathVariable("orderId") String orderId) {
        requireAdmin(request);
        orderService.sendOrder(orderId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "已发货");
        return result;
    }

    @GetMapping("/{orderId}/items")
    public List<com.wrf.entity.OrderItem> items(HttpServletRequest request,
                                                @PathVariable("orderId") String orderId) {
        requireAdmin(request);
        return orderService.showOrderDetail(orderId);
    }

    private com.wrf.entity.User requireUser(HttpServletRequest request) {
        var user = authSupport.current(request);
        if (user == null) {
            throw new ApiException(ApiCodes.NOT_LOGIN, "请先登录");
        }
        return user;
    }

    private void requireAdmin(HttpServletRequest request) {
        var user = requireUser(request);
        if (!authSupport.isAdmin(user)) {
            throw new ApiException(ApiCodes.FORBIDDEN, "请使用管理员账号登录");
        }
    }
}
