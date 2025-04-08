package com.example.mapper;

import com.example.entity.Message;
import com.example.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {

    void add(Message message);

    void update(Message message);

    void deleteById(@Param("id") Integer id);

    List<Message> selectByUsername(@Param("username") String username);

    List<Message> selectAll(Message message);
}