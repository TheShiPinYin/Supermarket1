package com.example.entity;

import lombok.Data;

@Data
public class Supplier {
    private Integer id;
    private String name;
    private String content;

    // 非数据库字段，用于批量导出时接收多个id
    private String ids;
    private String[] idsarr;

}
