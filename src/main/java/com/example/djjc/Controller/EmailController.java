package com.example.djjc.Controller;

import com.example.djjc.Common.R;
import com.example.djjc.Service.AuthUserService;
import com.example.djjc.Service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private AuthUserService authUserService;

    @PostMapping("/getcode")
    public R<String> getcode(@RequestParam("email") String email){

        return authUserService.sendcode(email);
    }
}
