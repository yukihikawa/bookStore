package com.wrf.web;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @program: bookStore
 * @description: 基本servlet
 * @author: Rifu Wu
 * @create: 2022-05-14 21:00
 **/

@Slf4j
public abstract class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    /*static Logger logger;

    static {
        logger = LoggerFactory.getLogger(BaseDao.class);
    }*/


    @Override
    //使用反射处理相应请求
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getParameter("action");
        log.info("post for " + action );
        try {
            Method method = getClass().getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            method.setAccessible(true);
            method.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getParameter("action");
        log.info("get for " + action );
        doPost(req, resp);
    }
}