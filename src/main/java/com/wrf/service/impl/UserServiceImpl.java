package com.wrf.service.impl;

import com.wrf.dao.impl.UserDaoImpl;
import com.wrf.pojo.User;
import com.wrf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: bookStore
 * @description: UserService实现类
 * @author: Rifu Wu
 * @create: 2022-05-10 22:44
 **/
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserDaoImpl userDao;

    @Override
    public void registUser(User user) {
        userDao.saveUser(user);
    }

    @Override
    public User login(User user) {
        return userDao.queryUserByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    @Override
    public boolean existsUsername(String username) {
        if(userDao.queryUserByUsername(username) == null)
            return false;
        return true;
    }
}