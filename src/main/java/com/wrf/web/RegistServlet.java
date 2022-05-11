package com.wrf.web;

import com.wrf.pojo.User;
import com.wrf.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: bookStore
 * @description: 注册，Regist.html页面的Servlet
 * @author: Rifu Wu
 * @create: 2022-05-10 23:42
 **/

@Component
public class RegistServlet extends AutowiredHttpServlet {
    @Autowired
    UserServiceImpl userService;



    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1、获取请求的参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String code = req.getParameter("code");

        System.out.println(username);
        System.out.println(password);
        System.out.println(email);
        System.out.println(code);

        String checkCode = "abcde";
        if("abcde".equalsIgnoreCase(code)){
            System.out.println(0);
            if(userService.existsUsername(username)){
                System.out.println(1);
                System.out.println("username [" + username +"] exists！");
                req.getRequestDispatcher("/pages/user/regist.html").forward(req, resp);
            }
            else {
                System.out.println(2);
                userService.registUser(new User(null, username, password, email));

                req.getRequestDispatcher("/pages/user/regist_success.html").forward(req, resp);
            }
        }
        else {
            System.out.println(3);
            System.out.println("verification code [" + code + "] error!");
            req.getRequestDispatcher("/pages/user/regist.html").forward(req, resp);
        }
    }

    @Override
    public void servlet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }


}