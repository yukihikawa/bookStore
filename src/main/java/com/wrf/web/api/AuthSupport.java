package com.wrf.web.api;

import com.wrf.entity.Cart;
import com.wrf.entity.User;
import com.wrf.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: bookStore
 * @description: SPA 接口的轻量会话支持：
 *               登录/注册成功时签发一个不透明 token（存于内存 Map），
 *               换回 session 属性中的用户；同时统一管理挂在 session 上的购物车。
 *               如需水平扩展可将 Map 换成 Redis。
 * @author: Rifu Wu
 **/
@Component
@Slf4j
@AllArgsConstructor
public class AuthSupport {

    static final String ATTR_CART = "cart";
    static final String ATTR_USER = "user";

    private static final int TOKEN_BYTES = 32;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final byte[] BASE62 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".getBytes();

    /** token -> userId（进程内维护） */
    private final Map<String, Integer> tokenMap = new ConcurrentHashMap<>();

    private final UserService userService;

    /**
     * 签发 token 并让当前 session 以指定用户登录
     */
    public String issueToken(HttpServletRequest request, User user) {
        String token = randomToken();
        tokenMap.put(token, user.getId());
        request.getSession().setAttribute(ATTR_USER, user);
        log.info("issued token for user [{}]", user.getUsername());
        return token;
    }

    /**
     * 校验 token，成功则返回 session 中的用户并已刷新登录态
     */
    public User authenticate(HttpServletRequest request, String token) {
        Integer userId = token == null ? null : tokenMap.remove(token);
        if (userId == null) {
            return null;
        }
        User user = userService.queryUserById(userId);
        if (user == null) {
            return null;
        }
        request.getSession().setAttribute(ATTR_USER, user);
        return user;
    }

    /** 登出：销毁会话（token 随会话失效后无法再换回用户） */
    public void logout(HttpServletRequest request) throws IOException {
        request.getSession().invalidate();
    }

    /**
     * 获取 session 中的用户（单 token 认证后会话保持登录态）
     */
    public User current(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(ATTR_USER);
    }

    /**
     * 获取（或初始化）session 购物车
     *
     * @param refreshSession 创建购物车后是否写回 session
     */
    public Cart cart(javax.servlet.http.HttpSession session, boolean refreshSession) {
        Cart cart = (Cart) session.getAttribute(ATTR_CART);
        if (cart == null && refreshSession) {
            cart = new Cart();
            session.setAttribute(ATTR_CART, cart);
        }
        return cart;
    }

    /** 管理员判定：与旧版 ManagerFilter 的规则保持一致（username == admin） */
    public boolean isAdmin(User user) {
        return "admin".equals(user.getUsername());
    }

    private static String randomToken() {
        byte[] buf = new byte[TOKEN_BYTES];
        RANDOM.nextBytes(buf);
        StringBuilder sb = new StringBuilder(TOKEN_BYTES * 2);
        for (byte b : buf) {
            sb.append(BASE62[(b >> 4) & 0x0F]).append(BASE62[b & 0x0F]);
        }
        return sb.toString();
    }
}
