package com.wrf.web.api;

import com.wrf.entity.User;
import com.wrf.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @program: bookStore
 * @description: SPA 认证相关接口
 *   GET  /api/auth/captcha  重新生成 kaptcha 验证码（图片仍由 web.xml 的 KaptchaServlet 提供 /kaptcha.jpg）
 *   POST /api/auth/register 注册（code 必填）
 *   POST /api/auth/login    登录（code 必填）
 *   GET  /api/auth/me       当前用户
 *   POST /api/auth/logout   登出
 * @author: Rifu Wu
 **/
@RestController
@RequestMapping("/api/auth")
@Slf4j
@AllArgsConstructor
public class AuthApiController {

    private final UserService userService;
    private final AuthSupport authSupport;

    @PostMapping("/login")
    public Map<String, Object> login(HttpServletRequest request,
                                     @RequestBody Map<String, String> body) {
        Map<String, Object> result = new LinkedHashMap<>();

        // 与旧版一致：验证码经 KAPTCHA_SESSION_KEY 校验（校验后立即失效）
        checkCaptcha(request, body.get("code"));

        User queried = userOf(body);
        User user = userService.login(queried);
        if (user == null) {
            throw new ApiException(ApiCodes.WRONG_CREDENTIALS, "用户名或密码错误");
        }

        String token = authSupport.issueToken(request, user);
        result.put("token", token);
        result.put("user", clean(user));
        return result;
    }

    @PostMapping("/register")
    public Map<String, Object> register(HttpServletRequest request,
                                        @RequestBody Map<String, String> body) {
        Map<String, Object> result = new LinkedHashMap<>();

        checkCaptcha(request, body.get("code"));

        User user = userOf(body);
        if (userService.existsUsername(user.getUsername())) {
            throw new ApiException(ApiCodes.USER_EXISTS, "用户名已存在");
        }

        userService.registUser(user);
        // 与旧版行为一致：注册成功后自动登录
        String token = authSupport.issueToken(request, user);
        result.put("token", token);
        result.put("user", clean(user));
        return result;
    }

    /** 供 web.xml 的 kaptcha.jsp 页面刷新验证码：旧版 /captcha.do 不存在，这里提供同款 GET 入口 */
    @GetMapping("/captcha")
    public Map<String, Object> captcha(HttpServletRequest request) {
        // KaptchaServlet 在 session 中生成 token（KAPTCHA_SESSION_KEY），这里只需强制建立 session
        request.getSession(true);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("captchaUrl", "/kaptcha.jpg");
        return result;
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpServletRequest request) {
        User user = authSupport.current(request);
        if (user == null) {
            throw new ApiException(ApiCodes.NOT_LOGIN, "未登录");
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("user", clean(user));
        return result;
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request) throws Exception {
        authSupport.logout(request);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "ok");
        return result;
    }

    /** 校验验证码，逻辑与旧版 UserController 一致：忽略大小写，校验后从 session 移除 */
    private void checkCaptcha(HttpServletRequest request, String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new ApiException(ApiCodes.INVALID_PARAM, "请输入验证码");
        }
        String token = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        request.getSession().removeAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if (token == null || !token.equalsIgnoreCase(code.trim())) {
            throw new ApiException(ApiCodes.INVALID_PARAM, "验证码错误");
        }
    }

    private static User userOf(Map<String, String> body) {
        User user = new User();
        user.setUsername(body.get("username"));
        user.setPassword(body.get("password"));
        user.setEmail(body.get("email"));
        return user;
    }

    private static Map<String, Object> clean(User user) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        map.put("admin", "admin".equals(user.getUsername()));
        return map;
    }
}
