package com.wrf.service.impl;

import com.wrf.AppConfig;
import com.wrf.Bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
class UserServiceImplTest {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    UserServiceImpl userService = context.getBean(UserServiceImpl.class);

    @Test
    void registUser() {
        /*userService.registUser(new User(null, "bbj168", "666666", "bbj168@qq.com"));
        userService.registUser(new User(null, "bbj168", "666666", "bbj168@qq.com"));*/
        userService.registUser(new User(null, "abc168", "666666", "abc168@qq.com"));
    }

    @Test
    void login() {
        log.info( userService.login(new User(null, "abc168", "666666", null))+"");
        log.info( userService.login(new User(null, "wrfabc", "123456", null))+"");
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