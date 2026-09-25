package com.wrf.web.api;

import com.wrf.entity.Book;
import com.wrf.entity.Cart;
import com.wrf.entity.CartItem;
import com.wrf.service.BookService;
import com.wrf.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: bookStore
 * @description: SPA 购物车相关接口（购物车仍挂在 session 上，与 JSP 版共享同一 session）
 * @author: Rifu Wu
 **/
@RestController
@RequestMapping("/api/cart")
@Slf4j
@AllArgsConstructor
public class CartApiController {

    private final BookService bookService;
    private final OrderService orderService;
    private final AuthSupport authSupport;

    /** 购物车概览：条目 + 总数 + 总价 */
    @GetMapping
    public Map<String, Object> items(HttpServletRequest request) {
        Cart cart = authSupport.cart(request.getSession(), true);
        return cartView(cart);
    }

    /** 加入购物车（库存校验：不允许加入超过当前库存的数量） */
    @PostMapping("/items")
    public Map<String, Object> add(HttpServletRequest request,
                                   @RequestBody Map<String, Object> body) {
        int bookId = parseId(body.get("id"));
        Book book = bookService.getById(String.valueOf(bookId));
        if (book == null) {
            throw new ApiException(ApiCodes.NOT_FOUND, "书籍不存在");
        }

        Cart cart = authSupport.cart(request.getSession(), true);
        int willBe = cart.getCount(bookId) + 1;
        if (willBe > book.getStock()) {
            throw new ApiException(ApiCodes.INVALID_PARAM,
                    "《" + book.getName() + "》库存不足（剩余 " + book.getStock() + "）");
        }
        cart.addItem(new CartItem(book.getId(), book.getName(), 1, book.getPrice(), book.getPrice()));
        request.getSession().setAttribute("lastname", book.getName());
        return cartView(cart);
    }

    /** 修改数量（库存校验：新数量不得超过当前库存） */
    @PutMapping("/items/{id}")
    public Map<String, Object> updateCount(HttpServletRequest request,
                                           @PathVariable("id") int id,
                                           @RequestBody Map<String, Object> body) {
        int count = body.get("count") instanceof Number
                ? ((Number) body.get("count")).intValue()
                : Integer.parseInt(String.valueOf(body.get("count")));
        if (count < 1) {
            throw new ApiException(ApiCodes.INVALID_PARAM, "数量不能小于 1");
        }
        Book book = bookService.getById(String.valueOf(id));
        if (book == null) {
            throw new ApiException(ApiCodes.NOT_FOUND, "书籍不存在");
        }
        if (count > book.getStock()) {
            throw new ApiException(ApiCodes.INVALID_PARAM,
                    "《" + book.getName() + "》库存不足（剩余 " + book.getStock() + "）");
        }
        Cart cart = authSupport.cart(request.getSession(), true);
        cart.updateCount(id, count);
        return cartView(cart);
    }

    /** 删除条目 */
    @DeleteMapping("/items/{id}")
    public Map<String, Object> delete(HttpServletRequest request,
                                      @PathVariable("id") int id) {
        Cart cart = authSupport.cart(request.getSession(), true);
        cart.deleteItem(id);
        return cartView(cart);
    }

    /** 清空购物车 */
    @DeleteMapping
    public Map<String, Object> clear(HttpServletRequest request) {
        Cart cart = authSupport.cart(request.getSession(), true);
        cart.clear();
        return cartView(cart);
    }

    /** 结算下单：创建订单并清空购物车，返回订单号。
     *  订单号为主键（时间戳+userId），同毫秒并发下单可能碰撞，
     *  碰撞时重试（会话购物车不变，最多 3 次）。 */
    @PostMapping("/checkout")
    public Map<String, Object> checkout(HttpServletRequest request) {
        Cart cart = authSupport.cart(request.getSession(), true);
        if (cart.getItems().isEmpty()) {
            throw new ApiException(ApiCodes.CART_EMPTY, "购物车为空");
        }
        var user = authSupport.current(request);
        if (user == null) {
            throw new ApiException(ApiCodes.NOT_LOGIN, "请先登录");
        }

        String orderId;
        int attempts = 0;
        while (true) {
            attempts++;
            try {
                orderId = orderService.createOrder(cart, user.getId());
                break;
            } catch (org.springframework.dao.DuplicateKeyException e) {
                if (attempts >= 3) {
                    throw new ApiException(ApiCodes.INTERNAL, "下单繁忙，请稍后重试");
                }
                log.warn("orderId collision (attempt {}), retrying", attempts);
            }
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("orderId", orderId);
        result.put("cart", cartView(cart));
        return result;
    }

    /**
     * 购物车序列化：条目按 bookId 列出
     */
    private Map<String, Object> cartView(Cart cart) {
        Map<String, Object> view = new LinkedHashMap<>();
        if (cart == null) {
            view.put("totalCount", 0);
            view.put("totalPrice", BigDecimal.ZERO);
            view.put("items", List.of());
            return view;
        }
        List<CartItem> list = cart.getItems().values().stream()
                .sorted((a, b) -> a.getId() - b.getId())
                .collect(Collectors.toList());
        view.put("totalCount", cart.getTotalCount());
        view.put("totalPrice", cart.getTotalPrice());
        view.put("items", list);
        return view;
    }

    private static int parseId(Object value) {
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException e) {
            throw new ApiException(ApiCodes.INVALID_PARAM, "参数 id 非法");
        }
    }
}
