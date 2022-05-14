package com.wrf.web;

import com.wrf.pojo.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: bookStore
 * @description: user模块
 * @author: Rifu Wu
 * @create: 2022-05-14 20:06
 **/
public class UserServlet extends BaseServlet {

    private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if(userService.existsUsername(username)){
            if(userService.login(new User(null, username, password, null)) != null) {
                System.out.println("login success!");
                req.getRequestDispatcher("/pages/user/login_success1.jsp").forward(req, resp);
            }
            else {
                req.setAttribute("msg", "密码错误");
                req.setAttribute("username", username);
                System.out.println("password error!");
                req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
            }
        }
        else {
            //吧回显表单项信息保存到Request域
            req.setAttribute("msg", "用户不存在");
            req.setAttribute("username", username);
            System.out.println("user does not exist!");
            req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
        }

    }

    private void regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1、获取请求的参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String code = req.getParameter("code");


        String checkCode = "abcde";
        if(checkCode.equalsIgnoreCase(code)){
            if(userService.existsUsername(username)){
                req.setAttribute("msg", "用户名已存在");
                req.setAttribute("username", username);
                req.setAttribute("email", email);
                System.out.println("username [" + username +"] exists！");
                req.getRequestDispatcher("/pages/user/regist.jsp").forward(req, resp);
            }
            else {
                userService.registUser(new User(null, username, password, email));
                req.getRequestDispatcher("/pages/user/regist_success.jsp").forward(req, resp);
            }
        }
        else {
            req.setAttribute("msg", "验证码错误");
            req.setAttribute("username", username);
            req.setAttribute("email", email);
            System.out.println("verification code [" + code + "] error!");
            req.getRequestDispatcher("/pages/user/regist.jsp").forward(req, resp);
        }
    }
}