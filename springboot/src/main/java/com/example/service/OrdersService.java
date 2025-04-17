package com.example.service;

import com.example.entity.Orders;
import com.example.mapper.OrdersMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {

    @Resource
    private OrdersMapper ordersMapper;

    public void add(Orders orders) {
        ordersMapper.insert(orders);
    }

    public PageInfo<Orders> selectPage(Integer pageNum, Integer pageSize, Orders orders) {
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

    public void update(Orders orders) {
        ordersMapper.updateById(orders);
    }

    public void deleteById(Integer id) {
        ordersMapper.deleteById(id);
    }

    public void deleteBatch(List<Orders> list) {
        for (Orders orders : list) {
            this.deleteById(orders.getId());
        }
    }

    public List<Orders> selectAll(Orders orders) {
        return ordersMapper.selectAll(orders);
    }

}