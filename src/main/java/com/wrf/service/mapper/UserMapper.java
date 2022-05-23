package com.wrf.service.mapper;

import com.wrf.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @program: bookStore
 * @description:
 * @author: Rifu Wu
 * @create: 2022-05-22 00:21
 **/

public interface UserMapper {
    /**根据用户名查询用户
     * @param username
     * @return
     */
    @Select("SELECT * FROM t_user WHERE username = #{username}")
    User queryUserByUsername(@Param("username") String username);

    /*@Select("SELECT * FROM t_user WHERE username = #{username} AND password = #{password}")
    User queryUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);*/

    /**验证用户名和密码
     * @param user
     * @return
     */
    @Select("SELECT * FROM t_user WHERE username = #{user.username} AND password = #{user.password}")
    User queryUserByUsernameAndPassword(@Param("user") User user);

    /*@Insert("INSERT INTO t_user(`username`,`password`,`email`) VALUES(#{username}, #{password}, #{email})")
    int saveUser(@Param("username") String username, @Param("password") String password, @Param("email") String email);*/

    /**插入一个用户项
     * @param user
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO t_user(username, password, email) VALUES(#{user.username}, #{user.password}, #{user.email})")
    void saveUser(@Param("user") User user);
}
