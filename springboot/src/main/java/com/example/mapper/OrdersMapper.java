package com.example.mapper;

import com.example.entity.Orders;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface OrdersMapper {

    List<Orders> selectAll(Orders orders);

    void insert(Orders orders);

    void updateById(Orders orders);

    @Delete("delete from orders where id = #{id}")
    void deleteById(Integer id);
}