package com.example.djjc.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.djjc.Common.R;

public interface AuthUserService {

    R<String> sendcode(String email);

}
