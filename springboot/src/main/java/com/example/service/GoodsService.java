package com.example.service;

import com.example.entity.Category;
import com.example.entity.Goods;
import com.example.mapper.GoodsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Resource
    GoodsMapper goodsMapper;

    public void add(Goods goods) {
        goodsMapper.insert(goods);
    }

    public PageInfo<Goods> selectPage(Integer pageNum, Integer pageSize, Category category) {
        PageHelper.startPage(pageNum, pageSize);
        // 这里假设 selectAll 方法可以根据 category 进行筛选
        List<Goods> list = goodsMapper.selectAll();
        return PageInfo.of(list);
    }

    public void update(Goods goods) {
        goodsMapper.updateById(goods);
    }

    public void deleteById(Integer id) {
        goodsMapper.deleteById(id);
    }

    public void deleteBatch(List<Goods> goodsList) {
        for (Goods goods : goodsList) {
            this.deleteById(goods.getId());
        }
    }

    public List<Goods> selectAll() {
        return goodsMapper.selectAll();
    }
}