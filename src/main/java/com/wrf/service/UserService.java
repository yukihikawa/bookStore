package com.wrf.service;

import com.wrf.entity.User;
import com.wrf.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@AllArgsConstructor
public class UserService {
    final
    UserMapper userMapper;

    public void registUser(User user){
        userMapper.saveUser(user);

    }

    public User login(User user){
        /*return userMapper.queryUserByUsernameAndPassword(user.getUsername(), user.getPassword());*/
        return userMapper.queryUserByUsernameAndPassword(user);
    }

    public boolean existsUsername(String username) {
        if(userMapper.queryUserByUsername(username) == null)
            return false;
        return true;
    }
}