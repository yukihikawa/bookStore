package com.wrf.service.mapper;

import com.wrf.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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

    @Select("SELECT * FROM t_user WHERE id = #{id}")
    User queryUserById(@Param("id") Integer id);

    /*@Select("SELECT * FROM t_user WHERE username = #{username} AND password = #{password}")
    User queryUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);*/

    /**密码已迁移为 BCrypt 哈希（见 PasswordHasher），不再在 SQL 中比对明文
     * @return 所有用户（启动迁移存量明文密码用）
     */
    @Select("SELECT * FROM t_user")
    List<User> queryAllUsers();

    /**更新密码（启动时把存量明文密码迁移为 BCrypt 哈希）*/
    @Update("UPDATE t_user SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Integer id, @Param("password") String password);

    /*@Insert("INSERT INTO t_user(`username`,`password`,`email`) VALUES(#{username}, #{password}, #{email})")
    int saveUser(@Param("username") String username, @Param("password") String password, @Param("email") String email);*/

    /**插入一个用户项
     * @param user
     */
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO t_user(username, password, email) VALUES(#{user.username}, #{user.password}, #{user.email})")
    void saveUser(@Param("user") User user);
}
