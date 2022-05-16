package com.wrf.web;

import com.wrf.AppConfig;
import com.wrf.Bean.User;
import com.wrf.service.impl.UserServiceImpl;
import com.wrf.utils.WebUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletConfig;
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
    UserServiceImpl userService;

    @Override
    public void init(ServletConfig config) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.userService  = context.getBean(UserServiceImpl.class);
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if(userService.existsUsername(username)){
            User user = WebUtils.copyParamToBean(req.getParameterMap(), new User());
            if(userService.login(user) != null) {
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
        /*String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");*/

        String code = req.getParameter("code");

        User user = WebUtils.copyParamToBean(req.getParameterMap(), new User());
        String checkCode = "abcde";
        if(checkCode.equalsIgnoreCase(code)){
            if(userService.existsUsername(user.getUsername())){
                req.setAttribute("msg", "用户名已存在");
                req.setAttribute("username", user.getUsername());
                req.setAttribute("email", user.getEmail());
                System.out.println("username [" + user.getUsername() +"] exists！");
                req.getRequestDispatcher("/pages/user/regist.jsp").forward(req, resp);
            }
            else {
                userService.registUser(user);
                req.getRequestDispatcher("/pages/user/regist_success.jsp").forward(req, resp);
            }
        }
        else {
            req.setAttribute("msg", "验证码错误");
            req.setAttribute("username", user.getUsername());
            req.setAttribute("email", user.getEmail());
            System.out.println("verification code [" + code + "] error!");
            req.getRequestDispatcher("/pages/user/regist.jsp").forward(req, resp);
        }
    }


}