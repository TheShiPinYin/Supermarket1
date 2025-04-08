package com.example.entity;

import lombok.Data;

@Data
public class Goods {

    private Integer id;
    private String name;
    private Double price;
    private String img;
    private Integer supplierId;
    private Integer categoryId;
    private Integer count;

    private String categoryName;
    private String supplierName;


}
