package com.wrf.service;

import com.wrf.Bean.User;
import com.wrf.mapper.UserMapper;
import com.wrf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: bookStore
 * @description: shix
 * @author: Rifu Wu
 * @create: 2022-05-22 00:48
 **/
@Component
@Transactional
public class UserService {
    @Autowired
    UserMapper userMapper;

    public void registUser(User user){
        userMapper.saveUser(user.getUsername(), user.getPassword(), user.getEmail());
    }

    public User login(User user){
        return userMapper.queryUserByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    public boolean existsUsername(String username) {
        if(userMapper.queryUserByUsername(username) == null)
            return false;
        return true;
    }
}