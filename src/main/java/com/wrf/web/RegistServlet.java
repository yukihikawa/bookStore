package com.wrf.web;

import com.wrf.AppConfig;
import com.wrf.pojo.User;
import com.wrf.service.impl.UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: bookStore
 * @description: 注册，Regist.html页面的Servlet
 * @author: Rifu Wu
 * @create: 2022-05-10 23:42
 **/


public class RegistServlet extends HttpServlet {

    UserServiceImpl userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.userService  = context.getBean(UserServiceImpl.class);
    }



    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1、获取请求的参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String code = req.getParameter("code");


        String checkCode = "abcde";
        if(checkCode.equalsIgnoreCase(code)){
            if(userService.existsUsername(username)){
                System.out.println("username [" + username +"] exists！");
                req.getRequestDispatcher("/pages/user/regist.jsp").forward(req, resp);
            }
            else {
                userService.registUser(new User(null, username, password, email));
                req.getRequestDispatcher("/pages/user/regist_success.jsp").forward(req, resp);
            }
        }
        else {
            System.out.println("verification code [" + code + "] error!");
            req.getRequestDispatcher("/pages/user/regist.jsp").forward(req, resp);
        }
    }
}