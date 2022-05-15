package com.wrf.dao.impl;

import com.wrf.AppConfig;
import com.wrf.dao.impl.BaseDaoImpl;
import com.wrf.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

class BaseDaoTest {
    BaseDaoImpl baseDao;
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        this.baseDao  = context.getBean(BaseDaoImpl.class);
    }

    @Test
    void update() {
        String sql = "insert into t_user(`username`,`password`,`email`) values(?, ?, ?)";
        int result = baseDao.update(sql,"admin22", "admin22", "admin22@atguigu.com");
        System.out.println(result);

    }

    @Test
    void queryForOne(){
        String sql = "SELECT * FROM t_user WHERE id = ?";
        User user = baseDao.queryForOne(User.class, sql, 1);
        System.out.println(user);
    }

    @Test
    void queryForList() {
        String sql = "SELECT * FROM t_user WHERE id < ?";
        List<User> userList = baseDao.queryForList(User.class, sql, 0);
        System.out.println(userList);
    }

    @Test
    void queryForSingleValue() {
        String sql = "SELECT email FROM t_user WHERE id = ?";
        System.out.println(baseDao.queryForSingleValue(sql, 1));
    }
}