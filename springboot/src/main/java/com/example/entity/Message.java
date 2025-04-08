package com.example.entity;

import lombok.Data;

/**
 * 留言信息
 */
@Data
public class Message {
    private Integer id;
    private String content;
    private String username;
    private String time;
}