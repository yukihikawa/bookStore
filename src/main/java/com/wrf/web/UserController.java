package com.wrf.web;

import com.wrf.entity.User;
import com.wrf.service.UserService;
import com.wrf.service.utils.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;

/**
 * @program: bookStore
 * @description: usercontroller
 * @author: Rifu Wu
 * @create: 2022-05-23 20:36
 **/
@Controller
@Slf4j
@AllArgsConstructor
public class UserController {
    public static final String KEY_USER = "__user__";

    UserService userService;

    @PostMapping("/login")
    private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String username = req.getParameter("username");
        /*
        String password = req.getParameter("password");
        */
        String code = req.getParameter("code");
        String token = (String) req.getSession().getAttribute(KAPTCHA_SESSION_KEY);
        req.getSession().removeAttribute(KAPTCHA_SESSION_KEY);
        log.info("Token is : " + token);

        if(token.equalsIgnoreCase(code)){
            if(userService.existsUsername(username)){
                User user = userService.login(WebUtils.copyParamToBean(req.getParameterMap(), new User()));
                if(user != null) {
                    //登陆成功
                    log.info(username + " login success!");
                    req.getSession().setAttribute("user", user);
                    req.getRequestDispatcher("/pages/user/login_success1.jsp").forward(req, resp);
                }
                else {
                    req.setAttribute("msg", "密码错误");
                    req.setAttribute("username", username);
                    log.info("password error!");
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
        else {
            req.setAttribute("msg", "验证码错误");
            req.setAttribute("username", username);
            log.info("verification code [" + code + "] error!");
            req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
        }



    }

    /**
     * 注册方法
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */

    @PostMapping("/register")
    private void register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1、获取请求的参数
        /*String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");*/

        String code = req.getParameter("code");
        String token = (String) req.getSession().getAttribute(KAPTCHA_SESSION_KEY);
        req.getSession().removeAttribute(KAPTCHA_SESSION_KEY);
        log.info("Token is : " + token);
        User user = WebUtils.copyParamToBean(req.getParameterMap(), new User());

        if(token.equalsIgnoreCase(code)){
            if(userService.existsUsername(user.getUsername())){
                req.setAttribute("msg", "用户名已存在");
                req.setAttribute("username", user.getUsername());
                req.setAttribute("email", user.getEmail());
                System.out.println("username [" + user.getUsername() +"] exists！");
                req.getRequestDispatcher("/pages/user/regist.jsp").forward(req, resp);
            }
            else {
                userService.registUser(user);
                req.getSession().setAttribute("user", user);
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

    @GetMapping("/logout")
    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //销毁Session中的登录信息
        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath());
    }
}