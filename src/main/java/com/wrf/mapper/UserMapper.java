package com.wrf.mapper;

import com.wrf.Bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @program: bookStore
 * @description:
 * @author: Rifu Wu
 * @create: 2022-05-22 00:21
 **/

public interface UserMapper {
    @Select("SELECT * FROM t_user WHERE username = #{username}")
    User queryUserByUsername(@Param("username") String username);

    @Select("SELECT * FROM t_user WHERE username = #{username} AND password = #{password}")
    User queryUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Insert("INSERT INTO t_user(`username`,`password`,`email`) VALUES(#{username}, #{password}, #{email})")
    int saveUser(@Param("username") String username, @Param("password") String password, @Param("email") String email);
}
