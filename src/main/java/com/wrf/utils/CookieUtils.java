package com.wrf.utils;

import javax.servlet.http.Cookie;

/**
 * @program: bookStore
 * @description: Cookie工具类
 * @author: Rifu Wu
 * @create: 2022-05-17 22:23
 **/
public class CookieUtils {


    /**
     * 查找指定名称cookie对象
     * @param name
     * @param cookies
     * @return
     */
    public static Cookie findCookie(String name, Cookie[] cookies){
        if(name == null || cookies == null || cookies.length == 0)
            return null;
        for(Cookie cookie : cookies){
            if(name.equals(cookie.getName()))
                return cookie;
        }
        return null;
    }
}