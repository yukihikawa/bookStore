package com.wrf.web.filter;

import com.wrf.entity.User;
import com.wrf.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @program: bookStore
 * @description: 管理员权限检查
 * @author: Rifu Wu
 * @create: 2022-05-21 20:17
 **/
@Slf4j
@Component
@AllArgsConstructor
public class ManagerFilter implements Filter {

    UserService userService;

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("reach the managerfilter");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        User user = (User) req.getSession().getAttribute("user");


        if(user == null)
            req.getRequestDispatcher("/pages/user/login.jsp").forward(servletRequest, servletResponse);
        else if (user.getUsername().equals("admin")) {
            log.info(user.toString());
            log.info("权限：" + user.getUsername());
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else {
            log.info(user.toString());
            log.info("权限：" + user.getUsername());
            String msg = "请使用管理员账号登录！";
            req.setAttribute("msg", msg);
            req.getRequestDispatcher("/pages/manager/manager.jsp").forward(servletRequest, servletResponse);
        }

    }

    public void destroy() {
    }
}