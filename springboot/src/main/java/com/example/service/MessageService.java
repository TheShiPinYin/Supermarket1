package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Message;
import com.example.entity.Notice;
import com.example.mapper.MessageMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Resource
    private MessageMapper messageMapper;

    public PageInfo<Message> selectPage(int pageNum, int pageSize, String username) {
        PageHelper.startPage(pageNum, pageSize);
        List<Message> messages = messageMapper.selectByUsername(username);
        return new PageInfo<>(messages);
    }

    // 其他方法保持不变
    public void add(Message message) {
        messageMapper.add(message);
    }

    public void update(Message message) {
        messageMapper.update(message);
    }

    public void deleteById(Integer id) {
        messageMapper.deleteById(id);
    }

    public List<Message> selectAll(Message message) {
        return messageMapper.selectAll(message);
    }
}