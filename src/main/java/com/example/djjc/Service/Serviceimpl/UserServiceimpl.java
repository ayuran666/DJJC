package com.example.djjc.Service.Serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.djjc.Entity.User;
import com.example.djjc.Mapper.UserMapper;
import com.example.djjc.Service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl extends ServiceImpl<UserMapper, User> implements UserService {
}
