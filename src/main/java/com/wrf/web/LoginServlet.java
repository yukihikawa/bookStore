package com.wrf.web;

import com.wrf.AppConfig;
import com.wrf.pojo.User;
import com.wrf.service.impl.UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: bookStore
 * @description: 处理登录请求
 * @author: Rifu Wu
 * @create: 2022-05-12 21:48
 **/
public class LoginServlet extends HttpServlet {
    UserServiceImpl userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.userService  = context.getBean(UserServiceImpl.class);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if(userService.existsUsername(username)){
            if(userService.login(new User(null, username, password, null)) != null) {
                System.out.println("login success!");
                req.getRequestDispatcher("/pages/user/login_success1.jsp").forward(req, resp);
            }
            else {
                System.out.println("password error!");
                req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
            }
        }
        else {
            System.out.println("user does not exist!");
            req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
        }

    }
}