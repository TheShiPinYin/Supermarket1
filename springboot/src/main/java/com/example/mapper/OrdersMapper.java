package com.example.mapper;

import com.example.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrdersMapper {
    // 插入订单信息
    void insert(Orders orders);

    // 根据订单 ID 删除订单信息
    void deleteById(Integer id);

    // 更新订单信息
    void updateById(Orders orders);

    // 根据条件查询所有订单信息
    List<Orders> selectAll(Orders orders);

    void deleteBatch(List<Orders> list);
}