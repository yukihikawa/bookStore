package com.wrf.service.impl;

import com.wrf.AppConfig;
import com.wrf.entity.User;
import com.wrf.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class UserServiceImplTest {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    UserService userService = context.getBean(UserService.class);

    @Test
    void registUser() {
        userService.registUser(new User(null, "bbj168", "666666", "bbj168@qq.com"));
        /*userService.registUser(new User(null, "bbj168", "666666", "bbj168@qq.com"));*/
        userService.registUser(new User(null, "abc168", "666666", "abc168@qq.com"));
    }

    @Test
    void login() {
        System.out.println( userService.login(new User(null, "abc168", "666666", null)));
    }

    @Test
    void existsUsername() {
        if (userService.existsUsername("wrfabc")) {
            System.out.println("用户名已存在！");
        } else {
            System.out.println("用户名可用！");
        }
    }
}