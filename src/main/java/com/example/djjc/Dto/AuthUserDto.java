package com.example.djjc.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDto {

    //用户名
    private String username;

    //密码
    private String password;

    //验证码
    private String code;

    //邮箱
    private String email ;

}
