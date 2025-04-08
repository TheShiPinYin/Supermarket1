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

}