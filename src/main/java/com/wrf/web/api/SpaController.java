package com.wrf.web.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: bookStore
 * @description: SPA 前端路由刷新回退（构建产物位于 webapp/app/）。
 *   Spring MVC 的静态资源处理器（/app/**）优先命中的是真实文件
 *   （app/index.html、app/assets/*…），只有路径不存在时才走到本控制器，
 *   把 history 模式的一级/深层路由（/app/books、/app/orders/123…）
 *   forward 到 /app/index.html，交由 Vue Router 渲染。
 * @author: Rifu Wu
 **/
@Controller
public class SpaController {

    @GetMapping({"/app", "/app/*", "/app/**"})
    public String spaFallback(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        return "forward:/app/index.html";
    }
}
