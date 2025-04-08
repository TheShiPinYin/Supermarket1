package com.example.mapper;

import com.example.entity.Goods;

import java.util.List;

public interface GoodsMapper {
    int insert(Goods goods);

    List<Goods> selectAll();

    List<Goods> selectPage();

    void updateById(Goods goods);

    int deleteById(Integer id);
}