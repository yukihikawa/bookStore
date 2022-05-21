package com.wrf.mapper;

import com.wrf.Bean.User;
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
    @Select("SELECT * FROM t_user WHERE username = #{id}")
    User getByUsername(@Param("username") String username);
}
