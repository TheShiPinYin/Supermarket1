package com.example.controller;

import cn.hutool.core.io.FileUtil;
import com.example.common.Result;
import com.example.exception.CustomerException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *  处理文件上传下载
 */
@RestController
@RequestMapping("/files")
public class FileController {

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        // 找到文件的位置
        String filePath = System.getProperty("user.dir") + "/files/";
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        byte[] bytes = file.getBytes();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();  // 文件的原始名称
        // 写入文件
        FileUtil.writeBytes(bytes, filePath + fileName);
        String url = "http://localhost:8080/files/download/" + fileName;
        return Result.success(url);
    }

    /**
     * 文件下载
     * 下载路径："http://localhost:8080/files/download/404.jpg"
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws Exception {
        // 找到文件的位置
        String filePath = System.getProperty("user.dir") + "/files/";  // 获取到当前项目的根路径
        String realPath = filePath + fileName;
        boolean exist = FileUtil.exist(realPath);
        if (!exist) {
            throw new CustomerException("文件不存在");
        }
        // 读取文件的字节流
        byte[] bytes = FileUtil.readBytes(realPath);
        ServletOutputStream os = response.getOutputStream();
        // 输出流对象把文件写出到客户端
        os.write(bytes);
        os.flush();
        os.close();
    }

}
