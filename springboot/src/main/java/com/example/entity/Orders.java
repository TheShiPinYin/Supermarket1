package com.example.entity;

import lombok.Data;

@Data
public class Orders {
    private Integer id;
    private String orderNo;
    private Integer supplierId;
    private String name;
    private Integer count;
    private Double total;
    private String time;

    private String supplierName;

    // 非数据库字段，用于批量导出时接收多个id
    private String ids;
    private String[] idsarr;


}