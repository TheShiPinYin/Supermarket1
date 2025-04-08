package com.example.service;

import com.example.entity.Orders;
import com.example.mapper.OrdersMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
            ordersMapper.deleteById(orders.getId());
        }
    }

    public List<Orders> selectAll(Orders orders) {
        return ordersMapper.selectAll(orders);
    }

    public void exportData(Orders orders, HttpServletResponse response) throws Exception {
        String ids = orders.getIds();
        if (StrUtil.isNotBlank(ids)) {
            String[] idArr = ids.split(",");
            orders.setIdsarr(idArr);
        }
        List<Orders> list = ordersMapper.selectAll(orders);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("orderNo", "订单编号");
        writer.addHeaderAlias("name", "商品名称");
        writer.addHeaderAlias("count", "商品数量");
        writer.addHeaderAlias("total", "订单总价");
        writer.addHeaderAlias("supplierName", "供货商");
        writer.addHeaderAlias("time", "订单时间");
        writer.setOnlyAlias(true);
        writer.write(list);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("订单信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
        os.close();
    }

    public void importData(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        reader.addHeaderAlias("订单编号", "orderNo");
        reader.addHeaderAlias("商品名称", "name");
        reader.addHeaderAlias("商品数量", "count");
        reader.addHeaderAlias("订单总价", "total");
        reader.addHeaderAlias("供货商", "supplierName");
        reader.addHeaderAlias("订单时间", "time");
        List<Orders> list = reader.readAll(Orders.class);
        for (Orders orders : list) {
            ordersMapper.insert(orders);
        }
    }
}