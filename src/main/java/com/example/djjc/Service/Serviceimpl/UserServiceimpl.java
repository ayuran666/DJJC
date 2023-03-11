package com.example.djjc.Service.Serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.djjc.Common.R;
import com.example.djjc.Dto.UserRegisterDto;
import com.example.djjc.Entity.User;
import com.example.djjc.Mapper.UserMapper;
import com.example.djjc.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Service
public class UserServiceimpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public Boolean registerEmailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        User one = this.getOne(queryWrapper);
        return !ObjectUtil.isEmpty(one);
    }

    @Override
    public R<String> register(UserRegisterDto userRegisterDto) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String co = operations.get(userRegisterDto.getEmail());
        if(co == null || !co.equals(userRegisterDto.getCode())){
            return R.error("验证码不正确!");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,userRegisterDto.getPhone());
        User one = this.getOne(queryWrapper);
        if(one != null){
            return R.error("手机号已被注册！");
        }
        userRegisterDto.setPassword(DigestUtils.md5DigestAsHex(userRegisterDto.getPassword().getBytes(StandardCharsets.UTF_8)));
        this.save(userRegisterDto);
        return R.success("注册成功！");
    }
}
