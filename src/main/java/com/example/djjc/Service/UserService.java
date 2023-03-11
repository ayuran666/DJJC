package com.example.djjc.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.djjc.Common.R;
import com.example.djjc.Dto.UserRegisterDto;
import com.example.djjc.Entity.User;

import javax.jws.soap.SOAPBinding;

public interface UserService extends IService<User> {
    Boolean registerEmailExist(String email);

    R<String> register(UserRegisterDto userRegisterDto);
}
