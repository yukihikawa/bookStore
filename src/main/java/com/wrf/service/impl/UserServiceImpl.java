package com.wrf.service.impl;

import com.wrf.dao.UserDao;
import com.wrf.dao.impl.UserDaoImpl;
import com.wrf.Bean.User;
import com.wrf.mapper.UserMapper;
import com.wrf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: bookStore
 * @description: UserService实现类
 * @author: Rifu Wu
 * @create: 2022-05-10 22:44
 **/
@Component
@Transactional
public class UserServiceImpl implements UserService {
    /*@Autowired
    UserMapper userMapper;*/

    @Autowired
    UserDao userDao;


    @Override
    public void registUser(User user) {
        userDao.saveUser(user);
    }

    @Override
    public User login(User user) {
        return userDao.queryUserByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    /*@Override
    public boolean existsUsername(String username) {
        if(userMapper.getByUsername(username) == null)
            return false;
        return true;
    }*/


    @Override
    public boolean existsUsername(String username) {
        if(userDao.queryUserByUsername(username) == null)
            return false;
        return true;
    }


}