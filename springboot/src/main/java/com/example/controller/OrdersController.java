package com.example.controller;

import com.example.common.Result;
import com.example.entity.Orders;
import com.example.service.OrdersService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    @PostMapping("/add")
    public Result add(@RequestBody Orders orders) {
        ordersService.add(orders);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Orders orders) {
        ordersService.update(orders);
        return Result.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable Integer id) {
        ordersService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Orders> list) {
        ordersService.deleteBatch(list);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Orders orders) {
        List<Orders> list = ordersService.selectAll(orders);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Orders orders) {
        PageInfo<Orders> info = ordersService.selectPage(pageNum, pageSize, orders);
        return Result.success(info);
    }

    @GetMapping("/export")
    public void exportData(Orders orders, HttpServletResponse response) throws IOException {
        try {
            ordersService.exportData(orders, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/import")
    public Result importData(@RequestParam("file") MultipartFile file) throws Exception {
        ordersService.importData(file);
        return Result.success();
    }
}