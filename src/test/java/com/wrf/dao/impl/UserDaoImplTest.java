package com.wrf.dao.impl;

import com.wrf.AppConfig;
import com.wrf.dao.UserDao;
import com.wrf.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class UserDaoImplTest {

    UserDaoImpl userDao;

    {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.userDao  = context.getBean(UserDaoImpl.class);
    }
    @Test
    public void queryUserByUsername() {
        if (userDao.queryUserByUsername("admin") == null ){
            System.out.println("用户名可用！");
        } else {
            System.out.println("用户名已存在！");
        }
    }
    @Test
    public void queryUserByUsernameAndPassword() {
        if ( userDao.queryUserByUsernameAndPassword("admin","admin1234") == null) {
            System.out.println("用户名或密码错误，登录失败");
        } else {
            System.out.println("查询成功");
        }
    }
    @Test
    public void saveUser() {
        System.out.println( userDao.saveUser(new User(null,"wzg168", "123456", "wzg168@qq.com")) );
    }
}