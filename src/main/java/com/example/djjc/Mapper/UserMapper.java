package com.example.djjc.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.djjc.Entity.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper extends BaseMapper<User> {

}
