package com.example.service;

import com.example.entity.Category;
import com.example.mapper.CategoryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    public void add(Category category) {
        categoryMapper.insert(category);
    }

    public PageInfo<Category> selectPage(Integer pageNum, Integer pageSize, Category category) {
        PageHelper.startPage(pageNum, pageSize);
        List<Category> list = categoryMapper.selectAll(category);
        return PageInfo.of(list);
    }

    public void update(Category category) {
        categoryMapper.updateById(category);
    }

    public void deleteById(Integer id) {
        categoryMapper.deleteById(id);
    }

    public void deleteBatch(List<Category> list) {
        for (Category category : list) {
            this.deleteById(category.getId());
        }
    }

    public List<Category> selectAll(Category category) {
        return categoryMapper.selectAll(category);
    }

    public void exportData(Category category, HttpServletResponse response) throws IOException {
        // 1. 拿到所有数据
        List<Category> list = this.selectAll(category);
        // 2. 构建Writer对象
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 3. 设置中文表头
        writer.addHeaderAlias("name", "分类名称");
        writer.addHeaderAlias("content", "分类说明");
        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        // 4. 写出数据到writer
        writer.write(list);
        // 5. 设置输出的文件的名称以及输出流的头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("分类信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        // 6. 写出到输出流 并关闭 writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
        os.close();
    }

    public void importData(MultipartFile file) throws IOException {
        //  1. 拿到输入流 构建 reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //  2. 通过Reader 读取 excel 里面的数据
        reader.addHeaderAlias("分类名称", "name");
        reader.addHeaderAlias("分类说明", "content");
        List<Category> list = reader.readAll(Category.class);
        // 3. 将数据写到数据库
        for (Category category : list) {
            this.add(category);
        }
    }
}