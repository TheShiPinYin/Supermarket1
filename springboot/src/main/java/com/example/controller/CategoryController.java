package com.example.controller;

import com.example.common.Result;
import com.example.entity.Category;
import com.example.service.CategoryService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping("/add")
    public Result add(@RequestBody Category category) {
        categoryService.add(category);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Category category) {
        categoryService.update(category);
        return Result.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Category> list) {
        categoryService.deleteBatch(list);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Category category) {
        List<Category> list = categoryService.selectAll(category);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Category category) {
        PageInfo<Category> info = categoryService.selectPage(pageNum, pageSize, category);
        return Result.success(info);
    }

    @GetMapping("/export")
    public void exportData(Category category, HttpServletResponse response) {
        try {
            categoryService.exportData(category, response);
        } catch (IOException e) {
            // 可以在这里添加日志记录，比如使用 slf4j
            e.printStackTrace();
            // 这里可以返回错误响应给前端，不过由于是 void 方法，需要调整设计
        }
    }

    @PostMapping("/import")
    public Result importData(@RequestPart("file") MultipartFile file) {
        try {
            categoryService.importData(file);
            return Result.success();
        } catch (IOException e) {
            // 可以在这里添加日志记录，比如使用 slf4j
            e.printStackTrace();
            return Result.error("导入数据失败: " + e.getMessage());
        }
    }
}