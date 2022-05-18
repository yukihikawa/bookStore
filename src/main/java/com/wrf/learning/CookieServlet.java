package com.wrf.learning;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: bookStore
 * @description:
 * @author: Rifu Wu
 * @create: 2022-05-18 13:21
 **/
public class CookieServlet extends HttpServlet {

    protected void getCookie(HttpServletRequest req, HttpServletResponse resp){
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies);
            /*resp.getWriter().write();*/
    }
}