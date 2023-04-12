package com.example.djjc.Service.Serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.djjc.Utils.ReisPreUtils;
import com.example.djjc.Common.R;
import com.example.djjc.Dto.UserRegisterDto;
import com.example.djjc.Entity.User;
import com.example.djjc.Mapper.UserMapper;
import com.example.djjc.Service.UserService;
import com.example.djjc.Utils.md5Utils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

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
        if(co == null){
            return R.error("验证码已过期!");
        }else if(!co.equals(userRegisterDto.getCode())){
            return R.error("验证码不正确！");
        }

        // 手机号查重
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,userRegisterDto.getPhone());
        User one = this.getOne(queryWrapper);
        if(one != null){
            return R.error("手机号已被注册！");
        }

        // 登录成功之后把redis里的缓存删掉
        if(Boolean.FALSE.equals(redisTemplate.delete(ReisPreUtils.EMAIL_CACHE_PREFIX + userRegisterDto.getEmail()))){
            return R.error("后台缓存异常！");
        }

        //对密码进行加密
        userRegisterDto.setPassWord(md5Utils.md5(userRegisterDto.getPassWord()));
        this.save(userRegisterDto);
        return R.success("注册成功！");
    }



    @Override
    public R<String> login(User user) {

        // 判断手机和邮箱都为空 或 都不为空的情况 返回错误
        if((!StringUtils.isEmpty(user.getEmail()) && !StringUtils.isEmpty(user.getPhone().toString())) ||
                (StringUtils.isEmpty(user.getEmail()) && StringUtils.isEmpty(user.getPhone().toString()))){
            return R.error("账户错误或为空！");
        }

        User one = null;

        // 判断是手机号登录
        if(StringUtils.isEmpty(user.getEmail()) && !StringUtils.isEmpty(user.getPhone().toString())){
            one = this.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, user.getPhone()));

            // 判断是邮箱登录
        }else if(!StringUtils.isEmpty(user.getEmail()) && StringUtils.isEmpty(user.getPhone().toString())){

            one = this.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, user.getEmail()));
        }

        if(ObjectUtils.isEmpty(one)) {
            return R.error("账户或密码错误!");
        }
        String password = md5Utils.md5(user.getPassWord());
        if (!one.getPassWord().equals(password)){
            return R.error("账户或密码错误！");

        }

        return R.success(user.getName());
    }
}
