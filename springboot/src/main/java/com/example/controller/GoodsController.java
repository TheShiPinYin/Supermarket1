package com.example.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Category;
import com.example.entity.Goods;
import com.example.service.GoodsService;
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
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    GoodsService goodsService;

    @PostMapping("/add")
    public Result add(@RequestBody Goods goods) {
        goodsService.add(goods);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Goods goods) {
        goodsService.update(goods);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        goodsService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Goods> list) {
        goodsService.deleteBatch(list);
        return Result.success();
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Category category) {
        PageInfo<Goods> pageInfo = goodsService.selectPage(pageNum, pageSize, category);
        return Result.success(pageInfo);
    }

    @GetMapping("/export")
    public void exportData(Goods goods, HttpServletResponse response) throws Exception {
        String ids = goods.getIds();
        if (StrUtil.isNotBlank(ids)) {
            String[] idArr = ids.split(",");
            goods.setIdsarr(idArr);
        }
        List<Goods> list = goodsService.selectAll();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("name", "商品名称");
        writer.addHeaderAlias("price", "商品价格");
        writer.addHeaderAlias("categoryName", "商品分类");
        writer.addHeaderAlias("supplierName", "供货商");
        writer.addHeaderAlias("count", "商品库存");
        writer.setOnlyAlias(true);
        writer.write(list);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("商品信息", StandardCharsets.UTF_8);
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
        reader.addHeaderAlias("name", "商品名称");
        reader.addHeaderAlias("price", "商品价格");
        reader.addHeaderAlias("categoryId", "商品分类");
        reader.addHeaderAlias("supplierId", "供货商");
        reader.addHeaderAlias("count", "商品库存");
        List<Goods> list = reader.readAll(Goods.class);
        for (Goods goods : list) {
            goodsService.add(goods);
        }
        return Result.success();
    }
}