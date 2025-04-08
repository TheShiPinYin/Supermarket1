package com.example.controller;

import com.example.common.Result;
import com.example.entity.Message;
import com.example.entity.Notice;
import com.example.service.MessageService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    // 分页查询留言
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize,
                             @RequestParam(value = "username", required = false) String username) {
        // 这里需要实现分页查询逻辑，假设 service 中提供了相应方法
        PageInfo<Message> messages = messageService.selectPage(pageNum, pageSize, username);
        return Result.success(messages);
    }

    // 新增留言
    @PostMapping("/add")
    public Result add(@RequestBody Message message) {
        messageService.add(message);
        return Result.success();
    }

    // 修改留言
    @PutMapping("/update")
    public Result update(@RequestBody Message message) {
        messageService.update(message);
        return Result.success();
    }

    // 删除留言
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        messageService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Message message) {
        List<Message> messageList = messageService.selectAll(message);
        return Result.success(messageList);
    }

}