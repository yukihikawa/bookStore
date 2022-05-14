package com.wrf.dao.impl;

import com.wrf.dao.BaseDao;
import com.wrf.dao.UserDao;
import com.wrf.pojo.User;
import org.springframework.stereotype.Component;

/**
 * @program: bookStore
 * @description: UserDao实现类
 * @author: Rifu Wu
 * @create: 2022-05-10 22:05
 **/
@Component
public class UserDaoImpl extends BaseDao implements UserDao {
    @Override
    public User queryUserByUsername(String username) {
        String sql = "SELECT * FROM t_user WHERE username = ?";
        return queryForOne(User.class, sql, username);

    }

    @Override
    public User queryUserByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM t_user WHERE username = ? AND password = ?";
        return queryForOne(User.class, sql, username, password);
    }

    @Override
    public int saveUser(User user) {
        String sql = "INSERT INTO t_user(`username`,`password`,`email`) VALUES(?, ?, ?)";
        return update(sql, user.getUsername(), user.getPassword(), user.getEmail());

    }
}