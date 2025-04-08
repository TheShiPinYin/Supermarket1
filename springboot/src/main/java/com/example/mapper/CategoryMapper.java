package com.example.mapper;

import com.example.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 插入分类信息
     * @param category 分类实体
     */
    void insert(Category category);

    /**
     * 根据条件查询所有分类信息
     * @param category 分类查询条件
     * @return 分类列表
     */
    List<Category> selectAll(@Param("category") Category category);

    /**
     * 根据 ID 更新分类信息
     * @param category 分类实体
     */
    void updateById(Category category);

    /**
     * 根据 ID 删除分类信息
     * @param id 分类 ID
     */
    void deleteById(@Param("id") Integer id);
}