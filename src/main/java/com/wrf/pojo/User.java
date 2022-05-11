package com.wrf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: bookStore
 * @description: JavaBean，对应t_user表
 * @author: Rifu Wu
 * @create: 2022-05-10 10:37
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
}