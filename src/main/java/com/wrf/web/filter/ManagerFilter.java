package com.wrf.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.LogRecord;

/**
 * @program: bookStore
 * @description: 管理员权限检查
 * @author: Rifu Wu
 * @create: 2022-05-21 20:17
 **/
public class ManagerFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        Object user = req.getSession().getAttribute("user");

        if(user == null)
            req.getRequestDispatcher("/pages/user/login.jsp").forward(servletRequest, servletResponse);
        else
            filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}