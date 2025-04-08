package com.example.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Supplier;
import com.example.service.SupplierService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Resource
    private SupplierService supplierService;

    @PostMapping("/add")
    public Result add(@RequestBody Supplier supplier) {
        supplierService.add(supplier);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Supplier supplier) {
        supplierService.update(supplier);
        return Result.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable Integer id) {
        supplierService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Supplier> list) {
        supplierService.deleteBatch(list);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Supplier supplier) {
        List<Supplier> list = supplierService.selectAll(supplier);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Supplier supplier) {
        PageInfo<Supplier> info = supplierService.selectPage(pageNum, pageSize, supplier);
        return Result.success(info);
    }

    @GetMapping("/export")
    public void exportData(Supplier supplier, HttpServletResponse response) throws Exception {
        String ids = supplier.getIds();
        if (StrUtil.isNotBlank(ids)) {
            String[] idArr = ids.split(",");
            supplier.setIdsarr(idArr);
        }
        List<Supplier> list = supplierService.selectAll(supplier);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("name", "供货商名称");
        writer.addHeaderAlias("content", "供货商说明");
        writer.setOnlyAlias(true);
        writer.write(list);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("供货商信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
        os.close();
    }

    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        reader.addHeaderAlias("供货商名称", "name");
        reader.addHeaderAlias("供货商说明", "content");
        List<Supplier> list = reader.readAll(Supplier.class);
        for (Supplier supplier : list) {
            supplierService.add(supplier);
        }
        return Result.success();
    }
}