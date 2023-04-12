package com.example.djjc.Controller;


import com.example.djjc.Common.R;
import com.example.djjc.Dto.UserRegisterDto;
import com.example.djjc.Entity.User;
import com.example.djjc.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public R<String> register(@RequestBody @Validated UserRegisterDto userRegisterDto){
        return userService.register(userRegisterDto);
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody User user){
        return userService.login(user);
    }
}
