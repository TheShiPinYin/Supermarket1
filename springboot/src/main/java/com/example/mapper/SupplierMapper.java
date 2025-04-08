package com.example.mapper;

import com.example.entity.Supplier;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface SupplierMapper {
    @Insert("insert into supplier (name, content) values (#{name}, #{content})")
    void insert(Supplier supplier);

    List<Supplier> selectAll(Supplier supplier);

    void updateById(Supplier supplier);

    @Delete("delete from supplier where id = #{id}")
    void deleteById(Integer id);
}