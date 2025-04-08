package com.example.entity;

import lombok.Data;

import java.util.List;

/**
 * 普通用户信息
 */
@Data
public class User extends Account {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String role;
    private String token;
    private String avatar;

    // 非数据库字段
    private String ids;
    private List<String> idsArr;

}