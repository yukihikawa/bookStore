package com.wrf.web.api;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: bookStore
 * @description: 仅对 /api 前缀的跨域请求自动回显 Origin，支持登录态 Cookie 同站直连
 * @author: Rifu Wu
 **/
@Slf4j
public class ApiCORSFilter implements Filter {

    static final String API_PREFIX = "/api";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 仅在路径命中 /api 且带 Origin 时动态回显（剥掉 contextPath 再比较）
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        String appPath = (ctx != null && uri.startsWith(ctx)) ? uri.substring(ctx.length()) : uri;
        if (appPath.startsWith(API_PREFIX)) {
            String origin = request.getHeader("Origin");
            if (origin != null && !origin.isEmpty() && !"null".equalsIgnoreCase(origin)) {
                response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Vary", "Origin");
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Token");
                response.setHeader("Access-Control-Max-Age", "86400");
            }

            // 预检请求直接放行
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
