package com.example.djjc.Service.Serviceimpl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.example.djjc.Utils.ReisPreUtils;
import com.example.djjc.Common.R;
import com.example.djjc.Dto.EmailDto;
import com.example.djjc.Service.AuthUserService;
import com.example.djjc.Service.EmailService;
import com.example.djjc.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    @Value("${code.dieTime}")
    private Long dietime;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Override
    public R<String> sendcode(String email) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        if(userService.registerEmailExist(email)){
            return R.error("邮箱已被注册");
        }
        // TODO
        //防刷校验
        TemplateEngine templates = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = templates.getTemplate("email-code.ftl");

        String code = operations.get(ReisPreUtils.EMAIL_CACHE_PREFIX + email);
        if(code == null){
            code = System.currentTimeMillis() + "_" + RandomUtil.randomNumbers(6);

            try {
                operations.set(ReisPreUtils.EMAIL_CACHE_PREFIX + email, code, dietime, TimeUnit.SECONDS);
            }catch (Exception e){
                return R.error("后台缓存服务异常");
            }
        }else {
            long l = Long.parseLong(code.split("_")[0]);
            if(System.currentTimeMillis() - l < 60000){
                return R.error("一分钟之内不要重复发送！");
            }
        }

        try {
            emailService.send(new EmailDto(Collections.singletonList(email),
                    "邮箱验证码", template.render(Dict.create().set("code", code.split("_")[1]))));
        }catch (Exception e){
            return R.error(e.getMessage());
        }

        return R.success("验证码发送成功!");
    }
}
