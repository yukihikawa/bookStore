package com.wrf.service;

import com.wrf.entity.User;
import com.wrf.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: bookStore
 * @description: shix
 * @author: Rifu Wu
 * @create: 2022-05-22 00:48
 **/
@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserService {
    final
    UserMapper userMapper;
    final PasswordHasher passwordHasher;

    /**注册：密码在入库前哈希（BCrypt）*/
    public void registUser(User user){
        user.setPassword(passwordHasher.hash(user.getPassword()));
        userMapper.saveUser(user);

    }

    /**登录：按用户名查用户，再用 BCrypt 校验密码，返回完整用户或 null */
    public User login(User user){
        User queried = userMapper.queryUserByUsername(user.getUsername());
        if (queried == null
                || user.getPassword() == null
                || !passwordHasher.matches(user.getPassword(), queried.getPassword())) {
            return null;
        }
        return queried;
    }

    public boolean existsUsername(String username) {
        if(userMapper.queryUserByUsername(username) == null)
            return false;
        return true;
    }

    public User queryUserById(Integer id) {
        return userMapper.queryUserById(id);
    }
}