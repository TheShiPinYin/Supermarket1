package com.example.service;

import com.example.entity.Supplier;
import com.example.mapper.SupplierMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    @Resource
    private SupplierMapper supplierMapper;

    public void add(Supplier supplier) {
        supplierMapper.insert(supplier);
    }

    public PageInfo<Supplier> selectPage(Integer pageNum, Integer pageSize, Supplier supplier) {
        PageHelper.startPage(pageNum, pageSize);
        List<Supplier> list = supplierMapper.selectAll(supplier);
        return PageInfo.of(list);
    }

    public void update(Supplier supplier) {
        supplierMapper.updateById(supplier);
    }

    public void deleteById(Integer id) {
        supplierMapper.deleteById(id);
    }

    public void deleteBatch(List<Supplier> list) {
        for (Supplier supplier : list) {
            supplierMapper.deleteById(supplier.getId());
        }
    }

    public List<Supplier> selectAll(Supplier supplier) {
        return supplierMapper.selectAll(supplier);
    }
}